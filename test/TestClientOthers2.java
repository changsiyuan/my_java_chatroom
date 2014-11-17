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
public class TestClientOthers2 {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public TestClientOthers2() {
        System.out.println("Please Login!");

        try {
            socket = new Socket(Const.IP, Const.Port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            MessageReSender resend = new MessageReSender();
            resend.start();

            String strOutput = "";
            String strInput = "";

            //test re-login chang
            strOutput = "/login chang";
            out.println(strOutput);
            strInput = in.readLine();
            if (strInput.equals("Name exist, please choose anthoer name!")) {
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

        @Override
        public void run() {
            try {
                while (!stopped) {
                    String str = in.readLine();
                    if (str != null) {
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
        new TestClientOthers2();
    }
}
