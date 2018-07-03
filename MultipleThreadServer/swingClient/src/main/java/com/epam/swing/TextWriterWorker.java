package com.epam.swing;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class TextWriterWorker extends SwingWorker<Void, Void> {
    private JLabel statusLabel;
    private JTextArea textArea;
    private JTextField portField;
    private JTextField hostField;
    private PrintStream socketOutStream;
    private JButton sendButton;
    private Socket socket;


    public TextWriterWorker(Socket socket, JLabel statusLabel, JTextArea textArea, JTextField portField, JTextField hostField, JButton sendButton) {
        this.statusLabel = statusLabel;
        this.textArea = textArea;
        this.portField = portField;
        this.hostField = hostField;
        this.socket = socket;
        this.sendButton = sendButton;
        try {
            this.socketOutStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(){
        try {
            statusLabel.setText("Status: Connected");

            statusLabel.setText("Status: Sending line to the server");
            socketOutStream.println(textArea.getText());
            try {
                if (socketOutStream.checkError()) {
                    throw new ConnectException();
                }else{
                    statusLabel.setText("Status: Text was succesfully sent");
                    socketOutStream.flush();
                }
            } catch (ConnectException e) {
                statusLabel.setText("Status: server seems to be closed, reconnect later");
            }

        }catch (IllegalArgumentException ex) {
            statusLabel.setText("Status: wrong host or port");
            ex.printStackTrace();
        }

        textArea.setText("");

        return null;
    }
}