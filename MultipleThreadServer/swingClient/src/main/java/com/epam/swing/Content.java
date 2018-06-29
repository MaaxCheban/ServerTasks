package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Content extends JPanel {
    private static final int PORT_COLUMNS = 10;
    private static final int HOST_COLUMNS = 15;
    private static final int TEXTAREA_COLUMNS = 30;
    private static final int TEXTAREA_ROWS = 5;
    private JLabel inputTextLabel;
    private JLabel inputPortLabel;
    private JLabel inputHostLabel;
    private JLabel statusLabel;
    private JTextArea textArea;
    private JTextField portField;
    private JTextField hostField;
    private JButton sendButton;

    private Content(ContentBuilder builder) {
        super(new MigLayout());
        statusLabel = builder.statusLabel;
        inputTextLabel = builder.inputTextLabel;
        inputPortLabel = builder.inputPortLabel;
        inputHostLabel = builder.inputHostLabel;
        textArea = builder.textArea;
        portField = builder.portField;
        hostField = builder.hostField;
        sendButton = builder.sendButton;
    }

    private class ConnectButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TextWriterWorker worker = new TextWriterWorker(statusLabel, textArea, portField, hostField);
            worker.execute();
        }
    }


    public void init() {
        sendButton.addActionListener(new ConnectButtonActionListener());
        this.add(inputTextLabel);
        this.add(textArea, "wrap");
        this.add(inputPortLabel);
        this.add(portField, "split 3");
        this.add(inputHostLabel, "gapleft 30");
        this.add(hostField, "wrap");
        this.add(sendButton, "wrap");
        this.add(statusLabel, "span 3");
    }

    public static class ContentBuilder {
        private JLabel inputTextLabel = new JLabel("Input your text:");
        private JLabel inputPortLabel = new JLabel("Specify port:");
        private JLabel inputHostLabel = new JLabel("Specify Host:");
        private JTextArea textArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
        private JTextField portField = new JTextField("8080", PORT_COLUMNS);
        private JTextField hostField = new JTextField("127.0.0.1", HOST_COLUMNS);
        private JButton sendButton = new JButton("Send");
        private JLabel statusLabel = new JLabel("Status: ");

        public ContentBuilder setInputTextLabel(String inputTextLabel) {
            this.inputTextLabel.setText(inputTextLabel);
            return this;
        }

        public ContentBuilder setInputPortLabel(String inputPortLabel) {
            this.inputPortLabel.setText(inputPortLabel);
            return this;
        }

        public ContentBuilder setInputHostLabel(String inputHostLabel) {
            this.inputHostLabel.setText(inputHostLabel);
            return this;
        }

        public ContentBuilder setTextField(String textArea) {
            this.textArea.setToolTipText(textArea);
            return this;
        }

        public ContentBuilder setPortField(String portFieldText) {
            this.portField.setText(portFieldText);
            return this;
        }

        public ContentBuilder setHostField(String hostFieldText) {
            this.hostField.setText(hostFieldText);
            return this;
        }

        public ContentBuilder setSendButton(String sendButtonText) {
            this.sendButton.setText(sendButtonText);
            return this;
        }

        public Content build() {
            return new Content(this);
        }
    }

    public String getInputTextLabelText() {
        return inputTextLabel.getText();
    }

    public String getInputPortLabelText() {
        return inputPortLabel.getText();
    }

    public String getInputHostLabelText() {
        return inputHostLabel.getText();
    }

    public String getTextAreaText() {
        return textArea.getText();
    }

    public String getPortFieldText() {
        return portField.getText();
    }

    public String getHostFieldText() {
        return hostField.getText();
    }

    public String getSendButtonText() {
        return sendButton.getText();
    }

    public String getStatusLabelText() {
        return statusLabel.getText();
    }
}
