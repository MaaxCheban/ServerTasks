package com.epam.swing;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TextWriterWorker extends SwingWorker<Void, Void> {
    private JLabel statusLabel;
    private JTextArea textArea;
    private JTextField portField;
    private JTextField hostField;


    public TextWriterWorker(JLabel statusLabel, JTextArea textArea, JTextField portField, JTextField hostField) {
        this.statusLabel = statusLabel;
        this.textArea = textArea;
        this.portField = portField;
        this.hostField = hostField;
    }

    @Override
    protected Void doInBackground() {
        try (Socket socket = new Socket(hostField.getText(), Integer.parseInt(portField.getText()))) {
            statusLabel.setText("Status: Connected");
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                statusLabel.setText("Status: Sending line to the server");
                out.write(textArea.getText() + "\r\n");
                statusLabel.setText("Status: Text was succesfully sent");
                out.flush();
            }
        } catch (UnknownHostException ex) {
            statusLabel.setText("Status: unknown host");
            ex.printStackTrace();
        } catch (IOException ex) {
            statusLabel.setText("Status: check text u have input, again");
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            statusLabel.setText("Status: wrong host or port");
            ex.printStackTrace();
        }
        finally {
            System.out.println("Socket is closed");
        }
        textArea.setText("");

        return null;
    }
}