package com.company;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.text.NumberFormat;

public class ConnectPanel extends JPanel {
    private static final int PORT_WIDTH = 200;
    private static final int PORT_HEIGHT = 70;
    private static final int PORT_DEFAULT = 1099;
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final int HOST_COLUMNS = 10;
    private static final String DEFAULT_HOST_NAME = "127.0.0.1";
    private static final String DEFAULT_PORT_LABEL = "Specify port:";
    private static final String DEFAULT_HOST_LABEL = "Specify host:";
    private static final String DEFAULT_CONNECT_BUTTON = "Connect";
    private final JLabel inputPortLabel;
    private final JLabel inputHostLabel;
    private final JFormattedTextField portField;
    private final JTextField hostField;
    private final JButton connectButton;
    private Content parentContentContainer;

    private ConnectPanel(ConnectPanelBuilder builder) {
        super(new MigLayout());
        inputPortLabel = builder.inputPortLabel;
        inputHostLabel = builder.inputHostLabel;
        portField = builder.portField;
        hostField = builder.hostField;
        connectButton = builder.connectButton;

        connectButton.addActionListener(new ConnectButtonActionListener());

        this.add(inputHostLabel, "span 2");
        this.add(hostField, "wrap");
        this.add(inputPortLabel, "span 2");
        this.add(portField, "wrap");
        this.add(connectButton, "wrap");
    }

    public void setParentContentContainer(Content parentContentContainer) {
        this.parentContentContainer = parentContentContainer;
    }

    private class ConnectButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (parentContentContainer == null) {
                throw new UnsupportedOperationException("Content is not defined");
            }

            CommunicationTask communicationTask = new CommunicationTask(hostField.getText(), Integer.parseInt(portField.getText()), parentContentContainer);
            communicationTask.start();

        }
    }

    public void setConnectButtonEnabled(boolean par) {
        connectButton.setEnabled(par);
    }

    public static class ConnectPanelBuilder {
        private JLabel inputPortLabel = new JLabel(DEFAULT_PORT_LABEL);
        private JLabel inputHostLabel = new JLabel(DEFAULT_HOST_LABEL);
        private JFormattedTextField portField = new JFormattedTextField();
        private JTextField hostField = new JTextField(DEFAULT_HOST_NAME, HOST_COLUMNS);
        private JButton connectButton = new JButton(DEFAULT_CONNECT_BUTTON);


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