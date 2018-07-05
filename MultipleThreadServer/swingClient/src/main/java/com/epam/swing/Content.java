package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Content extends JPanel {
    private static final String STATUS_DEFAULT = "Status: ";
    private static final int MAX_COUNT_OF_STRINGS_IN_QUEUE = 1;
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

        blockingQueue = new ArrayBlockingQueue<>(MAX_COUNT_OF_STRINGS_IN_QUEUE);

        statusLabel = new JLabel(STATUS_DEFAULT);

        this.add(connectPanel, "wrap");
        this.add(statusLabel, "wrap");
    }

    public void toggle() {
        if(Thread.currentThread().getName().equals("AWT-EventQueue-0")){
            remove(connectPanel);
            add(comunicationPanel, "wrap");
            updateUI();

            return;
        }

        SwingUtilities.invokeLater(() -> {
                remove(connectPanel);
                add(comunicationPanel, "wrap");
                updateUI();
            }
        );
    }

    public void setSendButtonEnabled(final boolean par){
        if(Thread.currentThread().getName().equals("AWT-EventQueue-0")){
            comunicationPanel.setSendButtonEnabled(par);
            return;
        }

        SwingUtilities.invokeLater(() -> comunicationPanel.setSendButtonEnabled(par));
    }

    public void setConnectButtonEnabled(final boolean par){
        if(Thread.currentThread().getName().equals("AWT-EventQueue-0")){
            connectPanel.setConnectButtonEnabled(par);
            return;
        }

        SwingUtilities.invokeLater(() -> connectPanel.setConnectButtonEnabled(par));
    }

    public void setStatusLabel(final String status){
        if(Thread.currentThread().getName().equals("AWT-EventQueue-0")){
            statusLabel.setText("Status :" + status);
            return;
        }

        SwingUtilities.invokeLater(() -> statusLabel.setText("Status :" + status));
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