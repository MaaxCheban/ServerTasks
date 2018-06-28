package com.epam.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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

            try (Socket socket = new Socket(hostField.getText(), Integer.parseInt(portField.getText()))) {
                statusLabel.setText("Status: Connected");
                try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                    statusLabel.setText("Status: Sending line to the server");
                    out.write(textField.getText() + "\r\n");
                    statusLabel.setText("Status: Text was succesfully sent");
                    out.flush();
                }

            } catch (UnknownHostException ex) {
                statusLabel.setText("Status: unknown host");
                ex.printStackTrace();
            } catch (IOException ex) {
                statusLabel.setText("Status: check text u have input again");
                ex.printStackTrace();
            } catch (IllegalArgumentException ex) {
                statusLabel.setText("Status: wrong host or port");
                ex.printStackTrace();
            }

            textField.setText("");
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
        }
        if (inputHostLabel != null && hostField != null) {
            this.add(inputHostLabel);
            this.add(hostField);
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
