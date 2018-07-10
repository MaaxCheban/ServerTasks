package com.epam.rmi;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class Server implements RemoteFileWriter{
     private final static int REGISTRY_PORT = 1099;
     private final static int REMOTE_OBJECT_CONNECTION_PORT = 0;

    public static void main(String args[]){
        int registryPort = REGISTRY_PORT;
        int remoteObjectConnectionPort = REMOTE_OBJECT_CONNECTION_PORT;

        if(args.length == 1){
            registryPort = Integer.parseInt(args[0]);
        }else if(args.length == 2){
            registryPort = Integer.parseInt(args[0]);
            remoteObjectConnectionPort = Integer.parseInt(args[1]);
        }

        Server server = new Server();
        server.start(registryPort, remoteObjectConnectionPort);
    }

    public void start(int registryPort, int remoteObjectConnectionPort){
        try{
            RemoteFileWriter stub = (RemoteFileWriter) UnicastRemoteObject.exportObject(this, remoteObjectConnectionPort);

            Registry registry = LocateRegistry.createRegistry(registryPort);

            registry.bind("RemoteFileWriter", stub);

            System.out.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String text, LocalDateTime localDateTime, long id) {
        System.out.println("Client (" + id +") has written " + text + " at " + localDateTime);

        File file = new File(buildFileName(id));

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        try (PrintStream outputStream = new PrintStream(new FileOutputStream(file, true))) {
            outputStream.println(text + " " + localDateTime);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String buildFileName(long id) {
        return "file " + id + ".txt";
    }
}
