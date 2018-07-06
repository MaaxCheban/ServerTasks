package com.epam.swing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;

public class CommunicationTask implements Runnable {
    private static final int DELAY_RECONNECTION_TIME = 700;
    private static final int COUNT_OF_THREADS = 1;
    private static final long NEW_CLIENT_ID = -1;
    private final Executor executor;
    private final InetSocketAddress inetSocketAddress;
    private final BlockingQueue<String> blockingQueue;
    private final Content content;
    private Socket socket;
    private PrintStream outputStream;
    private BufferedReader inputStream;
    private long userId = NEW_CLIENT_ID;

    public CommunicationTask(InetSocketAddress inetSocketAddress, Content content) {
        this.inetSocketAddress = inetSocketAddress;
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
        } catch (ConnectException | UnknownHostException e) {
            content.setStatusLabel("Unable to connect or host is wrong");
            content.setConnectButtonEnabled(true);
            content.setSendButtonEnabled(false);
            e.printStackTrace();
        }
    }

    private void communicate() throws ConnectException, UnknownHostException {
        try {
            while (true) {
                String text = blockingQueue.take();

                content.setSendButtonEnabled(false);
                outputStream.println(text);

                if (outputStream.checkError()) {
                    reconnect();    // if something went wrong connect exception will be thrown
                    outputStream.println(text);
                }

                if (!outputStream.checkError()) {
                    content.setStatusLabel("Text was successfully sent");
                }

                content.setSendButtonEnabled(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ConnectException();
        }
    }

    private void connect() throws ConnectException, UnknownHostException {
        content.setConnectButtonEnabled(false);
        try {
            if (socket != null) {
                socket.close();
            }
            socket = new Socket(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
            outputStream = new PrintStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            outputStream.println(userId);

            if(userId == -1){
                userId = Long.parseLong(inputStream.readLine());
            }

            content.setStatusLabel("Connected, waiting for text");
            content.setSendButtonEnabled(true);
        } catch (UnknownHostException e) {
            throw e;
        } catch (IOException e) {
            throw new ConnectException();
        }
    }

    private void reconnect() throws ConnectException, UnknownHostException {
        content.setStatusLabel("Server disconnected, trying to reconnect...");

        for (int i = 0; i < 10; i++) {
            try {
                connect();
                return;
            } catch (UnknownHostException e) {
                throw e;
            } catch (IOException e) {
                try {
                    Thread.sleep(DELAY_RECONNECTION_TIME);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    throw new ConnectException();
                }
                continue;
            }
        }

        throw new ConnectException();
    }
}