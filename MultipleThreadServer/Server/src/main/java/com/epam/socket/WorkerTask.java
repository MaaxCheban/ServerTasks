package com.epam.socket;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.util.Random;

public class WorkerTask implements Runnable {
    private final Socket socket;

    public WorkerTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        System.out.println("Got a client ");
        System.out.println();
        int id = -1;
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream out = new PrintStream(socket.getOutputStream())
        ) {

            id = Integer.parseInt(in.readLine());


            if(id == -1){
                id = new Random().nextInt(10000);
                out.println(id);
            }

            
            File file = new File(buildFileName(id));

            System.out.println(id);


            PrintStream outputToFile = new PrintStream(file);

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