package com.epam.socket;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.util.*;

public class WorkerTask implements Runnable {
    private static final int UNINITIALIZED = -1;
    private final Socket socket;
    private final PropertyManager propertyManager;

    public WorkerTask(Socket socket, PropertyManager propertyManager) {
        this.socket = socket;
        this.propertyManager = propertyManager;
    }

    @Override
    public void run() {
        long id = UNINITIALIZED;
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream out = new PrintStream(socket.getOutputStream())
        ) {
            id = Long.parseLong(in.readLine());

            if (id > getId()) {
                throw new HackerException();
            }

            if (id == UNINITIALIZED) {
                id = getId();
                out.println(id);
                updateId(id + 1);
            }

            propertyManager.addValueToList("active_users", String.valueOf(id)); // Hacker Exception is thrown when more than one unique user

            try (BufferedWriter filePrinter = new BufferedWriter(new FileWriter(buildFileName(id)))) {
                System.out.println("Got a client " + id);
                System.out.println();

                String line;
                while (true) {
                    line = in.readLine();
                    System.out.println("Entering client(" + id + ") line to the file ");
                    filePrinter.write(line + "\n");
                    filePrinter.flush();
                    System.out.println("Waiting for the next line...");
                    System.out.println();
                }
            }

        } catch (IOException | HackerException e) {
            e.printStackTrace();
        } finally {
            try {
                if (id != UNINITIALIZED) {
                    propertyManager.removeValueFromList("active_users", String.valueOf(id));
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long getId() {
        String counter = propertyManager.getProperty("counter");
        return Long.parseLong(counter);
    }

    private void updateId(long newId) {
        propertyManager.setProperty("counter", String.valueOf(newId));
    }

    private static String buildFileName(long id) {
        return "file " + id + ".txt";
    }
}