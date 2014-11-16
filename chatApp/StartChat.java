/*
author:changsiyuan
*/
package chatApp;

import client.Client;
import server.Server;

import java.util.Scanner;

public class StartChat {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Load application as server or client? S / C)");
        try {
            char answer = Character.toLowerCase(in.nextLine().charAt(0));
            if (answer == 's') {
                new Server();

            } else if (answer == 'c') {
                new Client();
            } else {
                System.out.println("Wrong app specification. Try again.");
            }
        } catch (Exception e){
            System.out.println("Error");
        } finally {
            System.out.println("Switching off application");
        }
    }
}
