package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.text.NumberFormat;

import static java.lang.Thread.sleep;


public class Content extends JPanel {
    private static final int PORT_WIDTH = 200;
    private static final int PORT_HEIGHT = 70;
    private static final int PORT_DEFAULT = 8080;
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final int HOST_COLUMNS = 15;
    private static final int TEXTAREA_COLUMNS = 30;
    private static final int TEXTAREA_ROWS = 5;
    private static final String DEFAULT_HOST_NAME = "127.0.0.1";
    private JLabel inputTextLabel;
    private JLabel inputPortLabel;
    private JLabel inputHostLabel;
    private JLabel statusLabel;
    private JTextArea textArea;
    private JFormattedTextField portField;
    private JTextField hostField;
    private JButton sendButton;
    private Socket socket;


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
            try {
                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());
                if (socket == null || socket.isClosed()) {
                    socket = new Socket(host, port);
                } else if (isDifferentSocket(socket, host, port)) {
                    socket.close();
                    socket = new Socket(host, port);
                }

                TextWriterWorker worker = new TextWriterWorker(socket, statusLabel, textArea, portField, hostField, sendButton);
                worker.execute();
            } catch (UnknownHostException e1) {
                statusLabel.setText("Status: Unknown host");
                e1.printStackTrace();
            } catch (NumberFormatException e1) {
                statusLabel.setText("Status: wrong port format");
            } catch (ConnectException e1) {
                statusLabel.setText("Status: Trying to reconnect...");

                Thread heartbeatThread = new Thread() {
                    private static final long heartbeatDelayMillis = 700;

                    public void run() {
                        sendButton.setEnabled(false);
                        int counter;
                        for (counter = 0; counter < 10; counter++) {
                            try {
                                if (socket == null) {
                                    socket = new Socket(hostField.getText(), Integer.parseInt(portField.getText()));
                                }
                                socket.getOutputStream().write(666);

                                statusLabel.setText("Status: Connected");
                                break;
                            } catch (IOException e) {
                                try {
                                    sleep(heartbeatDelayMillis);
                                } catch (InterruptedException e2) {
                                    e2.printStackTrace();
                                }
                                continue;
                            }
                        }

                        if (counter == 10) {
                            statusLabel.setText("Status: connection error");
                        }


                        sendButton.setEnabled(true);
                    }
                };
                heartbeatThread.start();


                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        public boolean isDifferentSocket(Socket socket, String host, int port) throws UnknownHostException {
            InetAddress inetAddress = InetAddress.getByName(host);
            if (socket.getInetAddress().getHostAddress().equals(inetAddress.getHostAddress()) && socket.getPort() == port) {
                return false;
            }
            return true;
        }
    }

    public void init() {
        sendButton.addActionListener(new ConnectButtonActionListener());
        this.add(inputTextLabel);
        this.add(textArea, "wrap");
        this.add(inputPortLabel);
        this.add(portField, "wrap");
        this.add(inputHostLabel);
        this.add(hostField, "wrap");
        this.add(sendButton, "wrap");
        this.add(statusLabel, "span 3");
    }

    public static class ContentBuilder {
        private JLabel inputTextLabel = new JLabel("Input your text:");
        private JLabel inputPortLabel = new JLabel("Specify port:");
        private JLabel inputHostLabel = new JLabel("Specify Host:");
        private JTextArea textArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
        private JFormattedTextField portField = new JFormattedTextField();
        private JTextField hostField = new JTextField(DEFAULT_HOST_NAME, HOST_COLUMNS);
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
            NumberFormat format = NumberFormat.getInstance();
            format.setGroupingUsed(false);

            NumberFormatter formatter = new NumberFormatter(format);
            formatter.setValueClass(Integer.class);
            formatter.setMinimum(MIN_PORT);
            formatter.setMaximum(MAX_PORT);
            formatter.setAllowsInvalid(false);

            portField = new JFormattedTextField(formatter);
            portField.setSize(PORT_WIDTH, PORT_HEIGHT);
            portField.setValue(PORT_DEFAULT);

            portField.setVisible(true);

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
