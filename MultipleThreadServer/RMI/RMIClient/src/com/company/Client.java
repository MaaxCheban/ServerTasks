package com.company;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {
    }

    public static void main(String[] args) {

        Frame frame = new Frame();

        JLabel inputTextLabel = new JLabel("Type some text:");
        JLabel inputPortLabel = new JLabel("Specify port:");
        JLabel inputHostLabel = new JLabel("Specify host:");


        JTextField textField = new JTextField(15);
        JTextField portField = new JTextField(4);
        JTextField hostField = new JTextField(8);


        JButton sendButton = new JButton("Send");

        Content content = new Content.ContentBuilder().setSendButton(sendButton)
                .setInputTextLabel(inputTextLabel).setTextField(textField)
                .setInputHostLabel(inputHostLabel).setHostField(hostField).setInputPortLabel(inputPortLabel).setPortField(portField)
                .setLayoutManager(new FlowLayout())
                .build();
        content.init();

        frame.add(content);
        frame.setVisible(true);

    }
}