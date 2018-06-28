package com.epam.socket.client;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8080;


    public static void main(String[] args) {
        String host = HOST;
        int port = PORT;

        if (args.length > 0) {
            String address[] = args[0].split(":");
            host = address[0];
            if(args.length > 1){
                port = Integer.parseInt(address[1]);
            }

        }

        new Client().communicateWithServer(host, port);
    }


    private static boolean mustStop(String line) {
        if (line == null || line.equals("stop")) {
            return true;
        }
        return false;
    }

    public void communicateWithServer(String host, int port) {

        try (Socket socket = new Socket(host, port)){

            System.out.println("Got a program");
            try (
                    OutputStream sout = socket.getOutputStream();
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sout));
                    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))
            ) {

                String line = null;
                System.out.println("Type in something and press enter. It will send it to the server. Type \"stop\" if you want to stop");
                System.out.println();


                while (true) {
                    line = keyboard.readLine();
                    System.out.println("Sending this line to the server...");
                    out.write(line + "\r\n");

                    if (mustStop(line)) {
                        break;
                    }

                    out.flush();
                    System.out.println("Enter another line or \"stop\", if you want to stop");
                    System.out.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}