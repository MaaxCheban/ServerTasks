package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Content extends JPanel {
    private static final String STATUS_DEFAULT = "Status: ";
    private final ConnectPanel connectPanel;
    private final ComunicationPanel comunicationPanel;
    private final JLabel statusLabel;
    private final BlockingQueue<String> blockingQueue;

    public Content(ConnectPanel connectPanel, ComunicationPanel comunicationPanel) {
        super(new MigLayout());

        connectPanel.setParentContentContainer(this);
        comunicationPanel.setParentContentContainer(this);

        this.connectPanel = connectPanel;
        this.comunicationPanel = comunicationPanel;

        blockingQueue = new ArrayBlockingQueue<>(20);

        statusLabel = new JLabel(STATUS_DEFAULT);

        this.add(connectPanel, "wrap");
        this.add(statusLabel, "wrap");
    }

    public void toggle() {
        final Content content = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                content.remove(connectPanel);
                content.add(comunicationPanel, "wrap");
                content.updateUI();
            }
        });
    }

    public void setSendButtonEnabled(final boolean par){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                comunicationPanel.setSendButtonEnabled(par);
            }
        });
    }

    public void setConnectButtonEnabled(final boolean par){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                connectPanel.setConnectButtonEnabled(par);
            }
        });
    }

    public void setStatusLabel(final String status){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText("Status :" + status);
            }
        });
    }

    public BlockingQueue<String> getBlockingQueue() {
        return blockingQueue;
    }

    public String getStatusLabelText() {
        return statusLabel.getText();
    }

    public ComunicationPanel getComunicationPanel() {
        return comunicationPanel;
    }
}