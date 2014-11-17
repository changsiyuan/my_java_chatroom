package test;

import chatApp.Const;
import client.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author changsiyuan
 */
public class TestClientMyself {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public TestClientMyself() {
        System.out.println("Please Login!");

        try {
            socket = new Socket(Const.IP, Const.Port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            MessageReSender resend = new MessageReSender();
            resend.start();

            String strOutput = "";
            String strInput = "";

            //test the invalid command when chang login
            strOutput = "login";
            out.println(strOutput);
            strInput = in.readLine();
            if (strInput.equals("Invalid Command!")) {
                System.out.println("This test is successful!");
            } else {
                System.out.println("The output is: " + strInput + "....it is not true");
            }

            //test the login process of chang
            strOutput = "/login chang";
            out.println(strOutput);
            strInput = in.readLine();
            if (strInput.equals("You have logined")) {
                System.out.println("This test is successful!");
            } else {
                System.out.println("The output is: " + strInput + "....it is not true");
            }

            //test chang talk to everyone
            strOutput = "haha";
            out.println(strOutput);
            strInput = in.readLine();
            if (strInput.equals("I talk:haha")) {
                System.out.println("This test is successful!");
            } else {
                System.out.println("The output is: " + strInput + "....it is not true");
            }

            //test chang only talk to si
            strOutput = "hehe[si]";
            out.println(strOutput);
            strInput = in.readLine();
            if (strInput.equals("I talk to si: hehe ")) {
                System.out.println("This test is successful!");
            } else {
                System.out.println("The output is: " + strInput + "....it is not true");
            }

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
    }

    public static void main(String[] args) {
        new TestClientMyself();
    }
}
