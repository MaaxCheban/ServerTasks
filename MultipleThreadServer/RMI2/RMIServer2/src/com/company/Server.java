package com.company;

import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Server implements RemoteFileWriter {
    private final static int REGISTRY_PORT = 1099;
    private final static int REMOTE_OBJECT_CONNECTION_PORT = 0;

    public void write(String text) {
        int id = new Random().nextInt(10000);
        System.out.println("Got a client " + id);
        System.out.println();

        File file = new File(buildFileName(id));

        try (PrintStream outputStream = new PrintStream(file)) {

            outputStream.println(text);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildFileName(int id) {
        return "file " + id + ".txt";
    }

    public static void main(String args[]) {
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
}