/*
author:changsiyuan
creation time:2014.11.16
*/
package client;

import chatApp.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public Client() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please Login!");

        try {
            socket = new Socket(Const.IP, Const.Port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(scan.nextLine());

            MessageReSender resend = new MessageReSender();
            resend.start();

            String str = "";
            while (!str.equals("exit")) {
                str = scan.nextLine();
                out.println(str);
            }
            resend.setStop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    private class MessageReSender extends Thread {

        private boolean stopped;

        public void setStop() {
            stopped = true;
        }

        @Override
        public void run() {
            try {
                while (!stopped) {
                    String str = in.readLine();
                    if (str!=null) {
                        System.out.println(str);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error while receiving message.");
                //e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
