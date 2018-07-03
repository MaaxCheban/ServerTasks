package com.epam.swing;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ReconnectionWorker extends SwingWorker<Socket, Void> {
    private Socket socket;
    private String host;
    private int port;
    private static final long delayMillis = 700;

    public ReconnectionWorker(Socket socket){
        this.socket = socket;
        this.host = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
    }

    @Override
    protected Socket doInBackground() throws Exception {
        int counter;

        for (counter = 0; counter < 10; counter++) {
            try {
                socket = new Socket(host, port);
                socket.getOutputStream().write(666);
                return socket;
            } catch (IOException e) {
                try {
                    sleep(delayMillis);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                continue;
            }
        }

        return null;
    }
}
