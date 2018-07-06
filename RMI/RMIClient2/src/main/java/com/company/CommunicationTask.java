package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;

public class CommunicationTask implements Runnable {
    private static final int DELAY_RECONNECTION_TIME = 700;
    private static final int COUNT_OF_THREADS = 1;
    private final Executor executor;
    private final String registryHost;
    private final int registryPort;
    private final BlockingQueue<String> blockingQueue;
    private final Content content;
    private Registry registry;
    private RemoteFileWriter stub;
    public int user_id = -1; /* -1 - means that user is new to the server */

    public CommunicationTask(String registryHost, int registryPort, Content content) {
        this.registryHost = registryHost;
        this.registryPort = registryPort;
        this.blockingQueue = content.getBlockingQueue();
        this.content = content;

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
        } catch (ConnectException e) {
            content.setStatusLabel("Unable to connect or host is wrong");
            content.setConnectButtonEnabled(true);
            content.setSendButtonEnabled(false);
            e.printStackTrace();
        }
    }

    private void communicate() throws ConnectException {
        try {
            while (true) {
                String text = blockingQueue.take();

                content.setSendButtonEnabled(false);
                System.out.println("Sending text");
                stub.write(text);

                content.setSendButtonEnabled(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ConnectException();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        content.setConnectButtonEnabled(false);
        try {
            Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);

            stub = (RemoteFileWriter)registry.lookup("RemoteFileWriter");

            content.setStatusLabel("Connected, waiting for text");
            content.setSendButtonEnabled(true);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

//    private void reconnect() throws ConnectException, UnknownHostException {
//        content.setStatusLabel("Server disconnected, trying to reconnect...");
//
//        for (int i = 0; i < 10; i++) {
//            try {
//                connect();
//                return;
//            } catch (UnknownHostException e) {
//                throw e;
//            } catch (IOException e) {
//                try {
//                    Thread.sleep(DELAY_RECONNECTION_TIME);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                    throw new ConnectException();
//                }
//                continue;
//            }
//        }
//
//        throw new ConnectException();
//    }
}