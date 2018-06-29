package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Content extends JPanel {
    private JLabel inputTextLabel;
    private JLabel inputPortLabel;
    private JLabel inputHostLabel;
    private JLabel statusLabel;
    private JTextField textField;
    private JTextField portField;
    private JTextField hostField;
    private JButton sendButton;

    private Content(ContentBuilder builder) {
        super(builder.layout == null ? new FlowLayout() : builder.layout);
        statusLabel = new JLabel("Status: ");
        inputTextLabel = builder.inputTextLabel;
        inputPortLabel = builder.inputPortLabel;
        inputHostLabel = builder.inputHostLabel;
        textField = builder.textField;
        portField = builder.portField;
        hostField = builder.hostField;
        sendButton = builder.sendButton;
    }

    public class ConnectButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Registry registry = LocateRegistry.getRegistry(hostField.getText(), Integer.parseInt(portField.getText()));
                RemoteFileWriter stub = (RemoteFileWriter) registry.lookup("RemoteFileWriter");
                stub.write(textField.getText());
                statusLabel.setText("Status: text was succesfully sent");
            } catch (RemoteException e1) {
                statusLabel.setText("Status: remote exception occured");
                e1.printStackTrace();
            } catch (NotBoundException e1) {
                statusLabel.setText("Status: Not found any object with the specified name");
                e1.printStackTrace();
            } catch(NumberFormatException e1){
                statusLabel.setText("Status: check your port value again");
                e1.printStackTrace();
            }
        }
    }


    public void init() {
        sendButton.addActionListener(new ConnectButtonActionListener());
        if (inputTextLabel != null && textField != null) {
            this.add(inputTextLabel);
            this.add(textField);
        }
        if (inputPortLabel != null && portField != null) {
            this.add(inputPortLabel);
            this.add(portField);
            portField.setText("1099");
        }
        if (inputHostLabel != null && hostField != null) {
            this.add(inputHostLabel);
            this.add(hostField);
            hostField.setText("127.0.0.1");
        }
        if (sendButton != null) {
            this.add(sendButton);
        }
        this.add(statusLabel);

    }

    public static class ContentBuilder {
        private JLabel inputTextLabel;
        private JLabel inputPortLabel;
        private JLabel inputHostLabel;
        private JTextField textField;
        private JTextField portField;
        private JTextField hostField;
        private JButton sendButton;
        private LayoutManager layout;

        public ContentBuilder setInputTextLabel(JLabel inputTextLabel) {
            this.inputTextLabel = inputTextLabel;
            return this;
        }

        public ContentBuilder setInputPortLabel(JLabel inputPortLabel) {
            this.inputPortLabel = inputPortLabel;
            return this;

        }

        public ContentBuilder setInputHostLabel(JLabel inputHostLabel) {
            this.inputHostLabel = inputHostLabel;
            return this;
        }

        public ContentBuilder setTextField(JTextField textField) {
            this.textField = textField;
            return this;
        }

        public ContentBuilder setPortField(JTextField portField) {
            this.portField = portField;
            return this;
        }

        public ContentBuilder setHostField(JTextField hostField) {
            this.hostField = hostField;
            return this;
        }


        public ContentBuilder setSendButton(JButton sendButton) {
            this.sendButton = sendButton;
            return this;
        }

        public ContentBuilder setLayoutManager(LayoutManager layout) {
            this.layout = layout;
            return this;
        }


        public Content build() {
            return new Content(this);
        }
    }

    public JLabel getInputTextLabel() {
        return inputTextLabel;
    }

    public JLabel getInputPortLabel() {
        return inputPortLabel;
    }

    public JLabel getInputHostLabel() {
        return inputHostLabel;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JTextField getPortField() {
        return portField;
    }

    public JTextField getHostField() {
        return hostField;
    }

    public JButton getSendButton() {
        return sendButton;
    }
}
