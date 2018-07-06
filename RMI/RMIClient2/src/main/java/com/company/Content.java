package com.company;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Content extends JPanel {
    private static final String STATUS_DEFAULT = "Status: ";
    private static final int MAX_COUNT_OF_STRINGS_IN_QUEUE = 1;
    private final ConnectPanel connectPanel;
    private final CommunicationPanel CommunicationPanel;
    private final JLabel statusLabel;
    private final BlockingQueue<String> blockingQueue;

    public Content(ConnectPanel connectPanel, CommunicationPanel CommunicationPanel) {
        super(new MigLayout());

        connectPanel.setParentContentContainer(this);
        CommunicationPanel.setParentContentContainer(this);

        this.connectPanel = connectPanel;
        this.CommunicationPanel = CommunicationPanel;

        blockingQueue = new ArrayBlockingQueue<>(MAX_COUNT_OF_STRINGS_IN_QUEUE);

        statusLabel = new JLabel(STATUS_DEFAULT);

        this.add(connectPanel, "wrap");
        this.add(statusLabel, "wrap");
    }

    private void invoke(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    public void toggle() {
        invoke(() -> {
                    remove(connectPanel);
                    add(CommunicationPanel, "wrap");
                    updateUI();
                }
        );
    }

    public void setSendButtonEnabled(final boolean par){
        invoke(() -> CommunicationPanel.setSendButtonEnabled(par));
    }

    public void setConnectButtonEnabled(final boolean par){
        invoke(() -> connectPanel.setConnectButtonEnabled(par));
    }

    public void setStatusLabel(final String status){
        invoke(() -> statusLabel.setText("Status: " + status));
    }

    public BlockingQueue<String> getBlockingQueue() {
        return blockingQueue;
    }

    public String getStatusLabelText() {
        return statusLabel.getText();
    }

    public CommunicationPanel getCommunicationPanel() {
        return CommunicationPanel;
    }
}