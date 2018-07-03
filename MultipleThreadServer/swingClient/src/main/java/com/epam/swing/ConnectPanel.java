package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;

public class ConnectPanel extends JPanel {
    private static final int PORT_WIDTH = 200;
    private static final int PORT_HEIGHT = 70;
    private static final int PORT_DEFAULT = 8080;
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final int HOST_COLUMNS = 10;
    private static final String DEFAULT_HOST_NAME = "127.0.0.1";
    private static final String DEFAULT_PORT_LABEL = "Specify port:";
    private static final String DEFAULT_HOST_LABEL = "Specify host:";
    private static final String DEFAULT_CONNECT_BUTTON = "Connect";
    private static final String DEFAULT_STATUS_LABEL = "Status:";
    private JLabel inputPortLabel;
    private JLabel inputHostLabel;
    private JLabel statusLabel;
    private JFormattedTextField portField;
    private JTextField hostField;
    private JButton connectButton;
    private Content parentContentContainer;


    private ConnectPanel(ConnectPanelBuilder builder) {
        super(new MigLayout());
        statusLabel = builder.statusLabel;
        inputPortLabel = builder.inputPortLabel;
        inputHostLabel = builder.inputHostLabel;
        portField = builder.portField;
        hostField = builder.hostField;
        connectButton = builder.connectButton;

        connectButton.addActionListener(new ConnectButtonActionListener());

        this.add(inputHostLabel, "span 2");
        this.add(hostField,"wrap");
        this.add(inputPortLabel, "span 2");
        this.add(portField, "wrap");
        this.add(connectButton, "wrap");
        this.add(statusLabel, "span 4");
    }

    public JLabel getInputPortLabel() {
        return inputPortLabel;
    }

    public void setInputPortLabel(JLabel inputPortLabel) {
        this.inputPortLabel = inputPortLabel;
    }

    public JLabel getInputHostLabel() {
        return inputHostLabel;
    }

    public void setInputHostLabel(JLabel inputHostLabel) {
        this.inputHostLabel = inputHostLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public JFormattedTextField getPortField() {
        return portField;
    }

    public void setPortField(JFormattedTextField portField) {
        this.portField = portField;
    }

    public JTextField getHostField() {
        return hostField;
    }

    public void setHostField(JTextField hostField) {
        this.hostField = hostField;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public void setConnectButton(JButton connectButton) {
        this.connectButton = connectButton;
    }

    public Content getParentContentContainer() {
        return parentContentContainer;
    }

    public void setParentContentContainer(Content parentContentContainer) {
        this.parentContentContainer = parentContentContainer;
    }

    private class ConnectButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(parentContentContainer == null){
                throw new UnsupportedOperationException("Content is not defined");
            }

            ConnectionThread connectionThread = new ConnectionThread(hostField.getText(), Integer.parseInt(portField.getText()));


            //parentContentContainer.toggle();
        }
    }

    public static class ConnectPanelBuilder {
        private JLabel inputPortLabel = new JLabel(DEFAULT_PORT_LABEL);
        private JLabel inputHostLabel = new JLabel(DEFAULT_HOST_LABEL);
        private JFormattedTextField portField = new JFormattedTextField();
        private JTextField hostField = new JTextField(DEFAULT_HOST_NAME, HOST_COLUMNS);
        private JButton connectButton = new JButton(DEFAULT_CONNECT_BUTTON);
        private JLabel statusLabel = new JLabel(DEFAULT_STATUS_LABEL);


        public ConnectPanelBuilder setInputPortLabelText(String inputPortLabel) {
            this.inputPortLabel.setText(inputPortLabel);
            return this;
        }

        public ConnectPanelBuilder setInputHostLabelText(String inputHostLabel) {
            this.inputHostLabel.setText(inputHostLabel);
            return this;
        }

        public ConnectPanelBuilder setPortFieldText(String portFieldText) {
            this.portField.setText(portFieldText);
            return this;
        }

        public ConnectPanelBuilder setHostFieldText(String hostFieldText) {
            this.hostField.setText(hostFieldText);
            return this;
        }

        public ConnectPanelBuilder setConnectButtonText(String connectButtonText) {
            this.connectButton.setText(connectButtonText);
            return this;
        }

        public ConnectPanel build() {
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

            return new ConnectPanel(this);
        }
    }

}
