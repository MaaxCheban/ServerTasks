package com.epam.swing;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;


public class ReconnectionTask implements Callable<Socket> {
    private static final int delayMillis = 700;
    private InetSocketAddress inetSocketAddress;
    private String text;
    private Socket socket;
    private JLabel statusLabel;

    public ReconnectionTask(InetSocketAddress inetSocketAddress, String text, JLabel statusLabel){
        this.inetSocketAddress = inetSocketAddress;
        this.text = text;
        this.statusLabel = statusLabel;
    }

    @Override
    public Socket call() {
        for(int i = 0; i < 10; i++){
            try {
                Socket socket = new Socket(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                printStream.println(text);

                if(printStream.checkError()){
                    throw new ConnectException();
                }else{
                    return socket;
                }
            } catch (ConnectException e) {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                continue;
            } catch (UnknownHostException e) {
                statusLabel.setText("Status: unkonow host");
                e.printStackTrace();
            } catch (IOException e) {
                statusLabel.setText("Status: error connecting to the server");
                e.printStackTrace();
            }
        }

        return null;
    }
}
