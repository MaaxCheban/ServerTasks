package com.epam.swing;

import java.io.IOException;
import java.net.Socket;

public class ConnectionThread extends Thread{
    Socket socket;
    String host;
    int port;

    public ConnectionThread(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void run(){
        try {
            socket = new Socket(host, port);
            System.out.println("Connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
