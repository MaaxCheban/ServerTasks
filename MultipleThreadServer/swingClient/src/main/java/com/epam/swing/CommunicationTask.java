package com.epam.swing;

import jdk.nashorn.internal.ir.Block;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;

public class CommunicationTask implements Runnable {
    private static final int DELAY_RECONNECTION_TIME = 700;
    private Socket socket;
    private JLabel statusLabel;
    private Executor executor;
    private InetSocketAddress inetSocketAddress;
    private BlockingQueue<String> blockingQueue;
    private JButton connectButton;
    private JButton sendButton;
    private PrintStream outputStream;
    private Content content;

    public CommunicationTask(InetSocketAddress inetSocketAddress, Content content, JButton connectButton, JButton sendButton) {
        this.inetSocketAddress = inetSocketAddress;
        this.blockingQueue = content.getBlockingQueue();
        this.statusLabel = content.getStatusLabel();
        this.connectButton = connectButton;
        this.sendButton = sendButton;
        this.content = content;

        executor = Executors.newFixedThreadPool(1);
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
            statusLabel.setText("Unable to connect");
            sendButton.setEnabled(false);
            connectButton.setEnabled(true);
            e.printStackTrace();
        }
    }

    private void communicate() throws ConnectException {
        try {
            while (true) {
                String text = blockingQueue.take();

                sendButton.setEnabled(false);
                outputStream.println(text);

                if (outputStream.checkError()) {
                    statusLabel.setText("Server disconnected, trying to reconnect...");
                    socket.close();

                    reconnect(text);

                    if(socket == null){
                        sendButton.setEnabled(false);
                        statusLabel.setText("Reconnect later");
                        break;
                    }else{
                        outputStream = new PrintStream(socket.getOutputStream());
                        statusLabel.setText("Reconnected");
                        sendButton.setEnabled(true);
                    }

                } else {
                    statusLabel.setText("Text was succesfully sent");
                }
                sendButton.setEnabled(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            throw new ConnectException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws ConnectException{
        connectButton.setEnabled(false);
        try {
            socket = new Socket(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
            outputStream = new PrintStream(socket.getOutputStream());
            statusLabel.setText("Connected, waiting for text");
            return;
        } catch (Exception e){
            throw new ConnectException();
        }

    }

    private void reconnect(String text) throws ConnectException {
        for (int i = 0; i < 10; i++) {
            try {
                Socket newSocket = new Socket(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                PrintStream printStream = new PrintStream(newSocket.getOutputStream());
                printStream.println(text);
                if (printStream.checkError()) {
                    Thread.sleep(DELAY_RECONNECTION_TIME);
                    continue;
                } else {
                    socket = newSocket;
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                statusLabel.setText("Unknown host");
                throw new ConnectException();
            } catch (IOException e) {
                continue;
            }
        }

        throw new ConnectException();
    }
}