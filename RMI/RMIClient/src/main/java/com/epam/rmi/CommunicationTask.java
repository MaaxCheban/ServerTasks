package com.epam.rmi;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.*;

public class CommunicationTask implements Runnable {
    private static final int DELAY_RECONNECTION_TIME = 700;
    private static final int COUNT_OF_THREADS = 1;
    public static final String REMOTE_OBJECT_NAME = "RemoteFileWriter";
    private final Executor executor;
    private final String host;
    private final int port;
    private final BlockingQueue<String> blockingQueue;
    private final Content content;
    private final long userId;
    private RemoteFileWriter stub;
    private Registry registry;

    public CommunicationTask(String host, int port, Content content) {
        this.host = host;
        this.port = port;
        this.blockingQueue = content.getBlockingQueue();
        this.content = content;

        userId = new Random().nextInt(10000);
        executor = Executors.newFixedThreadPool(COUNT_OF_THREADS);
    }

    public void start() {
        executor.execute(this);
    }

    @Override
    public void run() {
        try {
            connect();
            content.toggle();
            communicate();
        } catch (RemoteException e) {
            content.setStatusLabel("Remote exception occured");
            e.printStackTrace();
        } catch (NotBoundException e) {
            content.setStatusLabel("There is no object bound to a registry with that name");
            e.printStackTrace();
        }
        content.setConnectButtonEnabled(true);
        content.setSendButtonEnabled(false);
    }

    private void communicate() throws RemoteException, NotBoundException {
        try {
            while (true) {
                String text = blockingQueue.take();

                try {
                    content.setSendButtonEnabled(false);
                    stub.write(text, LocalDateTime.now(), userId);

                    content.setSendButtonEnabled(true);
                } catch (RemoteException e) {
                    reconnect();
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws RemoteException, NotBoundException {
        content.setConnectButtonEnabled(false);

        registry = LocateRegistry.getRegistry(host, port);

        stub = (RemoteFileWriter) registry.lookup(REMOTE_OBJECT_NAME);

        content.setStatusLabel("Connected, waiting for text");

        content.setSendButtonEnabled(true);
    }

    private void reconnect() throws RemoteException, NotBoundException {
        content.setStatusLabel("Server disconnected, trying to reconnect...");

        for (int i = 0; i < 10; i++) {
            try {
                connect();
                return;
            } catch (RemoteException e) {
                try {
                    Thread.sleep(DELAY_RECONNECTION_TIME);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    throw new RemoteException();
                }
                continue;
            }
        }

        throw new RemoteException();
    }
}