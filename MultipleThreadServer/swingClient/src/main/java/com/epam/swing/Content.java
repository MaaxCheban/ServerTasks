package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;


public class Content extends JPanel {
    private ConnectPanel connectPanel;
    private ComunicationPanel comunicationPanel;


    public Content(ConnectPanel connectPanel, ComunicationPanel comunicationPanel) {
        super(new MigLayout());
        connectPanel.setParentContentContainer(this);
        comunicationPanel.setParentContentContainer(this);
        this.connectPanel = connectPanel;
        this.comunicationPanel = comunicationPanel;

        this.add(connectPanel);
    }

    public void toggle(){
        this.remove(connectPanel);
        this.add(comunicationPanel);
        this.updateUI();
    }

//    private class ConnectButtonActionListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
//                String host = hostField.getText();
//                int port = Integer.parseInt(portField.getText());
//                if (socket == null || socket.isClosed()) {
//                    socket = new Socket(host, port);
//                } else if (isDifferentSocket(socket, host, port)) {
//                    socket.close();
//                    socket = new Socket(host, port);
//                }
//
//                TextWriterWorker worker = new TextWriterWorker(socket, statusLabel, textArea, portField, hostField, sendButton);
//                worker.execute();
//            } catch (UnknownHostException e1) {
//                statusLabel.setText("Status: Unknown host");
//                e1.printStackTrace();
//            } catch (NumberFormatException e1) {
//                statusLabel.setText("Status: wrong port format");
//            } catch (ConnectException e1) {
//                statusLabel.setText("Status: Trying to reconnect...");
//
//                Thread heartbeatThread = new Thread() {
//                    private static final long heartbeatDelayMillis = 700;
//
//                    public void run() {
//                        sendButton.setEnabled(false);
//                        PrintStream printStream = null;
//                        try {
//                            printStream = new PrintStream(socket.getOutputStream());
//                        } catch (IOException e2) {
//                            e2.printStackTrace();
//                            return;
//                        }
//                        int counter;
//                        for (counter = 0; counter < 10; counter++) {
//                            try {
//                                if (socket == null) {
//                                    socket = new Socket(hostField.getText(), Integer.parseInt(portField.getText()));
//                                }
//                                printStream.write(666);
//
//                                statusLabel.setText("Status: Connected");
//                                break;
//                            } catch (IOException e) {
//                                try {
//                                    sleep(heartbeatDelayMillis);
//                                } catch (InterruptedException e2) {
//                                    e2.printStackTrace();
//                                }
//                                continue;
//                            }
//                        }
//
//                        if (printStream.checkError()) {
//                            statusLabel.setText("Status: connection error");
//                        }
//                        sendButton.setEnabled(true);
//                    }
//                };
//                heartbeatThread.start();
//
//
//                e1.printStackTrace();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//
//        public boolean isDifferentSocket(Socket socket, String host, int port) throws UnknownHostException {
//            InetAddress inetAddress = InetAddress.getByName(host);
//            if (socket.getInetAddress().getHostAddress().equals(inetAddress.getHostAddress()) && socket.getPort() == port) {
//                return false;
//            }
//            return true;
//        }
//    }


}