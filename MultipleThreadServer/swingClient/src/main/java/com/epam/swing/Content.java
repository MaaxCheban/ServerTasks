package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Content extends JPanel {
    private ConnectPanel connectPanel;
    private ComunicationPanel comunicationPanel;
    private JLabel statusLabel;
    private BlockingQueue<String> blockingQueue;


    public Content(ConnectPanel connectPanel, ComunicationPanel comunicationPanel) {
        super(new MigLayout());
        connectPanel.setParentContentContainer(this);
        comunicationPanel.setParentContentContainer(this);
        this.connectPanel = connectPanel;
        this.comunicationPanel = comunicationPanel;

        blockingQueue = new ArrayBlockingQueue<>(20);


        statusLabel = new JLabel("Status: ");

        this.add(connectPanel, "wrap");
        this.add(statusLabel);
    }

    public void toggle(){
        JLabel statusLabelClone = statusLabel;
        this.remove(connectPanel);
        this.remove(statusLabel);
        this.add(comunicationPanel, "wrap");
        this.add(statusLabelClone);
        this.updateUI();
    }

    public BlockingQueue<String> getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public ComunicationPanel getComunicationPanel() {
        return comunicationPanel;
    }

    public void setComunicationPanel(ComunicationPanel comunicationPanel) {
        this.comunicationPanel = comunicationPanel;
    }
}