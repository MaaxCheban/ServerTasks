package com.epam.swing;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ReconnectionWorker extends SwingWorker<Socket, Socket> {
    private Socket socket;
    private String host;
    private int port;
    private static final long delayMillis = 700;

    public ReconnectionWorker(Socket socket, String host, int port){

        this.socket = socket;
        this.host = host;
        this.port = port;
    }

    @Override
    protected Socket doInBackground() throws Exception {
        int counter;
        for (counter = 0; counter < 10; counter++) {
            try {
                if (socket == null) {
                    socket = new Socket(host, port);
                }
                socket.getOutputStream().write(666);

                break;
            } catch (IOException e) {
                try {
                    sleep(delayMillis);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                continue;
            }
        }

        if (counter == 10) {
            return null;
        }

        return socket;
    }
}
