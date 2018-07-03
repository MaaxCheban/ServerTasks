package com.epam.swing;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class CallableSocketConnector implements Callable<Socket> {
    Socket socket;
    String host;
    int port;

    public CallableSocketConnector(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public Socket call() throws IOException {
        socket = new Socket(host, port);
        
        return socket;
    }
}
