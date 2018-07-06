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
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream out = new PrintStream(socket.getOutputStream())
        ) {

            int id = Integer.parseInt(in.readLine());

            if (id == -1) {
                id = new Random().nextInt(10000);
                out.println(id);
            }

            try (BufferedWriter filePrinter = new BufferedWriter(new FileWriter(buildFileName(id), true))) {
                System.out.println("Got a client " + id);
                System.out.println();

                String line;
                while (true) {
                    line = in.readLine();
                    if (line == null) {
                        System.out.println("Socket " + id + " is closed");
                        throw new IOException();
                    }
                    System.out.println("Entering client(" + id + ") line to the file ");
                    filePrinter.write(line + "\n");
                    filePrinter.flush();
                    System.out.println("Waiting for the next line...");
                    System.out.println();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String buildFileName(int id) {
        return "file " + id + ".txt";
    }

}