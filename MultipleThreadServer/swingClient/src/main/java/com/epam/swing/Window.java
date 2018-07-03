package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class Window extends JFrame{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private Content content;

    public Window(Content content) {
        this("Swing application", WIDTH, HEIGHT, content);
    }


    public Window(String title, int width, int height, Content content) {
        super.setTitle(title);
        super.setSize(width, height);
        super.setLocationRelativeTo(null);
        super.setLayout(new MigLayout());
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(content);
    }
}
