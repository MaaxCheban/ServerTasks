package com.epam.swing;

import javax.swing.*;
import java.io.*;

public class TextWriterWorker extends SwingWorker<Void, Void> {
    private JLabel statusLabel;
    private JTextArea textArea;
    private JTextField portField;
    private JTextField hostField;
    private PrintStream socketOutStream;
    private BufferedReader socketInStream;


    public TextWriterWorker(OutputStream socketOutStream, InputStream socketInStream, JLabel statusLabel, JTextArea textArea, JTextField portField, JTextField hostField) {
        this.statusLabel = statusLabel;
        this.textArea = textArea;
        this.portField = portField;
        this.hostField = hostField;
        this.socketOutStream = new PrintStream(socketOutStream);
        this.socketInStream = new BufferedReader(new InputStreamReader(socketInStream));
    }

    @Override
    protected Void doInBackground() {
        try {
            statusLabel.setText("Status: Connected");

            statusLabel.setText("Status: Sending line to the server");
            socketOutStream.println(textArea.getText());
            statusLabel.setText("Status: Text was succesfully sent");
            socketOutStream.flush();

        }catch (IllegalArgumentException ex) {
            statusLabel.setText("Status: wrong host or port");
            ex.printStackTrace();
        }

        textArea.setText("");

        return null;
    }
}