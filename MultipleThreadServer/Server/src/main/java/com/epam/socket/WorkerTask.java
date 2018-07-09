package com.epam.socket;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.util.*;

public class WorkerTask implements Runnable {
    private static final int UNINITIALIZED = -1;
    private final Socket socket;
    private static PropertyManager propertyManager;

    public WorkerTask(Socket socket, PropertyManager propertyManager) throws FileNotFoundException {
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
                throw new Hacker_Exception();
            }

            if (id == UNINITIALIZED) {
                id = getId();
                out.println(id);
                updateId(id + 1);
            }

            propertyManager.addValueToList("Active_users", String.valueOf(id)); // Hacker Exception is thrown when more than one unique user

            try (BufferedWriter filePrinter = new BufferedWriter(new FileWriter(buildFileName(id), true))) {
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Hacker_Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(id != UNINITIALIZED){
                    propertyManager.removeValueFromList("Active_users", String.valueOf(id));
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static long getId() {
        String counter = propertyManager.getProperty("Counter");
        return Long.parseLong(counter);
    }

    private static void updateId(long newId) {
        propertyManager.setProperty("Counter", String.valueOf(newId));
    }


    private static String buildFileName(long id) {
        return "file " + id + ".txt";
    }

}