package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ComunicationPanel extends JPanel{
    private static final int TEXTAREA_COLUMNS = 30;
    private static final int TEXTAREA_ROWS = 5;
    private JLabel inputTextLabel;
    private JLabel statusLabel;
    private JTextArea textArea;
    private JButton sendButton;
    private Content parentContentContainer;

    private ComunicationPanel(CommunicationPanelBuilder builder){
        super(new MigLayout());
        inputTextLabel = builder.inputTextLabel;
        statusLabel = builder.statusLabel;
        textArea = builder.textArea;
        sendButton = builder.sendButton;

        this.add(inputTextLabel, "span 2");
        this.add(textArea, "wrap");
        this.add(sendButton, "wrap");
        this.add(statusLabel, "span 2");
    }

    public static class CommunicationPanelBuilder {
        private JLabel inputTextLabel = new JLabel("Input text: ");
        private JLabel statusLabel = new JLabel("Status: ");
        private JTextArea textArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
        private JButton sendButton = new JButton("Send");

        public CommunicationPanelBuilder setTextAreaText(String textAreaText) {
            this.textArea.setText(textAreaText);
            return this;
        }

        public CommunicationPanelBuilder setTextAreaLabelText(String textAreaLabelText) {
            this.inputTextLabel.setText(textAreaLabelText);
            return this;
        }

        public CommunicationPanelBuilder setSendButtonText(String sendButtonText) {
            this.sendButton.setText(sendButtonText);
            return this;
        }

        public ComunicationPanel build() {
            return new ComunicationPanel(this);
        }
    }


    public static int getTextareaColumns() {
        return TEXTAREA_COLUMNS;
    }

    public static int getTextareaRows() {
        return TEXTAREA_ROWS;
    }

    public JLabel getInputTextLabel() {
        return inputTextLabel;
    }

    public void setInputTextLabel(JLabel inputTextLabel) {
        this.inputTextLabel = inputTextLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public Content getParentContentContainer() {
        return parentContentContainer;
    }

    public void setParentContentContainer(Content parentContentContainer) {
        this.parentContentContainer = parentContentContainer;
    }
}
