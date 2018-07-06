package com.epam.socket;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final int PORT = 8080;
    private static final String PROPERTY_FILE_NAME = "src/resources/server_info.properties";
    private PropertyManager propertyManager;
    public Server(){
        propertyManager = new PropertyManager(PROPERTY_FILE_NAME);
    }

    public static void main(String[] args)    {
        int port = PORT;
        if(args.length != 0){
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server();
        server.start(port);
    }

    public void start(int port){
        try (ServerSocket ss = new ServerSocket(port)){
            ExecutorService executor = Executors.newCachedThreadPool();
            System.out.println("Waiting for a client...");
            while(true){
                Socket socket = ss.accept();
                executor.execute(new WorkerTask(socket, propertyManager));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}