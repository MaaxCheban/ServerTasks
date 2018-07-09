package com.epam.socket;

import java.net.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 8080;
    private static final String PROPERTY_FILE_NAME = "server_info.properties";
    private final PropertyManager propertyManager;

    public Server(String propertyFileName) {
        propertyManager = new PropertyManager(propertyFileName);
    }

    public static void main(String[] args) {
        int port = PORT;
        String propertyFileName = PROPERTY_FILE_NAME;
        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
            if (args.length == 2) {
                propertyFileName = args[1];
            }
        }

        Server server = new Server(propertyFileName);
        server.start(port);
    }

    public void start(int port) {
        try (ServerSocket ss = new ServerSocket(port)) {
            ExecutorService executor = Executors.newCachedThreadPool();
            System.out.println("Waiting for a client...");
            while (true) {
                Socket socket = ss.accept();
                executor.execute(new WorkerTask(socket, propertyManager));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}