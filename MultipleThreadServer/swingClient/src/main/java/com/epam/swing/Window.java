package com.epam.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class Window extends JFrame{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private Content content;

    public Window() {
        this("Swing application", WIDTH, HEIGHT);
    }


    public Window(String title, int width, int height) {
        super.setTitle(title);
        super.setSize(width, height);
        super.setLocationRelativeTo(null);
        super.setLayout(new MigLayout());
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void init() {
        content.init();
        this.add(content);
        this.setVisible(true);
    }
}
