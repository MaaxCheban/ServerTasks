package com.epam.socket;

import java.net.*;
import java.io.*;
import java.util.Random;

public class Server {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        int port = PORT;
        if(args.length != 0){
            port = Integer.parseInt(args[0]);
        }

        try ( ServerSocket ss = new ServerSocket(port)){
            System.out.println("Waiting for a client...");
            while (true) {

                int id = new Random().nextInt(10000);
                System.out.println("Got a client " + id);
                System.out.println();

                File f = new File(buildFileName(id));

                try (
                        Socket socket = ss.accept();
                        InputStream sin = socket.getInputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(sin));
                        PrintStream outputStream = new PrintStream(f)
                ) {

                    String line = null;
                    while (true) {
                        line = in.readLine();

                        if (mustStop(line)) {
                            break;
                        }

                        System.out.println("Entering clients line to the file ");
                        outputStream.println(line);

                        System.out.println("Waiting for the next line...");
                        System.out.println();
                    }

                }finally{
                    System.out.println("Socket " + id + " is closed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String buildFileName(int id) {
        return "file " + id + ".txt";
    }

    private static boolean mustStop(String line) {
        if (line == null || line.equals("stop")) {
            return true;
        }
        return false;
    }
}