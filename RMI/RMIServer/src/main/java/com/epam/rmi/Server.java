package com.epam.rmi;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server implements RemoteFileWriter {
    private static final long UNINITIALIZED_USER = -1;
    private final static int REGISTRY_PORT = 1099;
    private final static int REMOTE_OBJECT_CONNECTION_PORT = 0;
    private ActiveUsersScannerTask activeUsersScannerTask;
    private long clientsCounter = 25;
    private final ConcurrentMap<Long, LocalTime> activeUsers;

    public Server() {
        activeUsers = new ConcurrentHashMap<>();
        activeUsersScannerTask = new ActiveUsersScannerTask(activeUsers);
    }

    public static void main(String args[]) {
        int registryPort = REGISTRY_PORT;
        int remoteObjectConnectionPort = REMOTE_OBJECT_CONNECTION_PORT;

        if (args.length == 1) {
            registryPort = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            registryPort = Integer.parseInt(args[0]);
            remoteObjectConnectionPort = Integer.parseInt(args[1]);
        }

        Server server = new Server();
        server.start(registryPort, remoteObjectConnectionPort);
    }

    public void start(int registryPort, int remoteObjectConnectionPort) {
        try {
            RemoteFileWriter stub = (RemoteFileWriter) UnicastRemoteObject.exportObject(this, remoteObjectConnectionPort);

            Registry registry = LocateRegistry.createRegistry(registryPort);

            registry.bind("RemoteFileWriter", stub);

            activeUsersScannerTask.start();

            System.out.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(OutputData outputData, long id) {
        if (!activeUsers.containsKey(id)) {
            System.out.println("User have to reconnect");
            return;

        }

        activeUsers.replace(id, LocalTime.now());

        System.out.println("Client (" + id + ") has written " + outputData.getText() + " at " + outputData.getLocalDateTime());

        File file = new File(buildFileName(id));

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        try (PrintStream outputStream = new PrintStream(new FileOutputStream(file, true))) {
            outputStream.println(outputData.getText() + " " + outputData.getLocalDateTime());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public long init() throws RemoteException {
        long id = clientsCounter++;
        activeUsers.put(id, LocalTime.now());
        return id;
    }

    private static String buildFileName(long id) {
        return "file " + id + ".txt";
    }
}