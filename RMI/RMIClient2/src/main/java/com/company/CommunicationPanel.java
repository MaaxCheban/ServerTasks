package com.company;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommunicationPanel extends JPanel {
    private static final int TEXTAREA_COLUMNS = 30;
    private static final int TEXTAREA_ROWS = 5;
    private final JLabel inputTextLabel;
    private final JTextArea textArea;
    private final JButton sendButton;
    private Content parentContentContainer;

    public JTextArea getTextArea() {
        return textArea;
    }

    private CommunicationPanel(CommunicationPanelBuilder builder) {
        super(new MigLayout());
        inputTextLabel = builder.inputTextLabel;
        textArea = builder.textArea;
        sendButton = builder.sendButton;

        sendButton.addActionListener(new SendButtonActionListener());

        this.add(inputTextLabel, "span 2");
        this.add(textArea, "wrap");
        this.add(sendButton, "wrap");
    }

    private class SendButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (parentContentContainer == null) {
                throw new UnsupportedOperationException("Content is not defined");
            }

            try {
                parentContentContainer.getBlockingQueue().put(textArea.getText());
                textArea.setText("");
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static class CommunicationPanelBuilder {
        private JLabel inputTextLabel = new JLabel("Input text: ");
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

        public CommunicationPanel build() {
            sendButton.setEnabled(false);
            return new CommunicationPanel(this);
        }
    }

    public void setSendButtonEnabled(boolean par) {
        sendButton.setEnabled(par);
    }

    public void setParentContentContainer(Content parentContentContainer) {
        this.parentContentContainer = parentContentContainer;
    }

}