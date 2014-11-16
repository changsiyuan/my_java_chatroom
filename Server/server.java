/*
author:changsiyuan
creation time:2014.11.16
*/
package server;

import chatApp.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private ConcurrentHashMap<String, Connection> usersName = new ConcurrentHashMap<>();         //usersName contains all the name and sockets of users
    private ServerSocket server;

    public Server() {
        System.out.println("Starting Server");
        try {
            server = new ServerSocket(Const.Port);
            while (true) {
                Socket socket = server.accept();
                Connection con = new Connection(socket);
                connections.add(con);
                con.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeAll() {
        try {
            server.close();
            synchronized (connections) {
                Iterator<Connection> iterator = connections.iterator();
                while (iterator.hasNext()) {
                    ((Connection) iterator.next()).close();
                }
            }
            System.out.println("Server is offline");
        } catch (Exception e) {
            System.err.println("Threads had not been stopped!");
        }
    }

    private class Connection extends Thread {

        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;
        private String name = "";

        public Connection(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                close();
            }
        }

        @Override
        public void run() {
            try {
                boolean b = true;
                do {
                    name = in.readLine();
                    String standardName = "/login\\s\\w\\w*";
                    if (name.matches(standardName)) {
                        if (usersName.containsKey(name.split("\\s")[1])) {
                            out.println("Name exist, please choose anthoer name!");
                            b = false;
                        } else {
                            usersName.put(name.split("\\s")[1], this);
                            out.println("You have logined");
                            othersPrint(name.split("\\s")[1], name.split("\\s")[1] + " has logined");
                            b = true;
                        }
                    } else {
                        out.println("Invalid Command!");
                        b = false;
                    }
                } while (!b);

                String message = null;
                while (true) {
                    message = in.readLine();
                    if (message.equals("quit")) {
                        out.println("You have quit this chat!");
                        othersPrint(name.split("\\s")[1], name.split("\\s")[1] + " has quit this chat.");
                        usersName.remove(name.split("\\s")[1]);
                        break;
                    } else if (message.equals("who")) {
                        out.println(usersName.size());
                    } else if (message.contains("[") || message.contains("]")) {
                        try {
                            String pickName = message.substring(message.indexOf("[") + 1, message.indexOf("]"));   //catch the name in [] when talking
                            String validMessage = message.replace(message.substring(message.indexOf("["), message.indexOf("]") + 1), " ");  //catch the message do not contains name
                            if (!(usersName.containsKey(pickName))) {
                                out.println(pickName + " is not online now");
                            } else if (pickName.equals(name.split("\\s")[1])) {
                                out.println("Please do not talk to yourself!");
                            } else {
                                out.println("I talk to " + pickName + ": " + validMessage);
                                someonePrint(pickName, name.split("\\s")[1] + " say something to you: " + validMessage);
                            }
                        } catch (Exception e) {
                            System.out.println("the person you talk to is not online or you input the wrong command");
                        }
                    } else {
                        out.println("I talk:" + message);
                        synchronized (connections) {
                            othersPrint(name.split("\\s")[1], name.split("\\s")[1] + " say: " + message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void othersPrint(String hisOwnName, String print) {
            Iterator iter = usersName.keySet().iterator();
            while (iter.hasNext()) {
                String name = iter.next().toString();
                if (!(name.equals(hisOwnName))) {
                    someonePrint(name, print);
                }
            }
        }

        public void someonePrint(String name, String print) {
            usersName.get(name).out.println(print);
        }

        public void close() {
            try {
                in.close();
                out.close();
                socket.close();
                connections.remove(this);
                if (connections.size() == 0) {
                    Server.this.closeAll();
                    System.exit(0);
                }
            } catch (Exception e) {
                System.err.println("Threads had not been stopped!");
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
