package com.epam.socket;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class WorkerTask implements Runnable {
    private final Socket socket;

    public WorkerTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        int id = new Random().nextInt(10000);
        System.out.println("Got a client " + id);
        System.out.println();

        File f = new File(buildFileName(id));

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream outputToFile = new PrintStream(f)
        ) {
            String line;
            while (true) {
                line = in.readLine();
                if(line == null){
                    throw new IOException();
                }
                System.out.println("Entering clients line to the file ");
                outputToFile.println(line);
                System.out.println("Waiting for the next line...");
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                System.out.println("Socket " + id + " is closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String buildFileName(int id) {
        return "file " + id + ".txt";
    }

}