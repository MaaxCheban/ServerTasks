package com.epam.swing;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private JPanel content;

    public Frame() {
        this("Swing application", 950, 600, null, new GridBagLayout());
    }

    public Frame(String title) {
        this(title, 950, 600, null, new GridBagLayout());
    }

    public Frame(String title, int width, int height) {
        this(title, width, height, null, new GridBagLayout());
    }

    public Frame(String title, int width, int height, Component component) {
        this(title, width, height, component, new GridBagLayout());
        init();
    }

    public Frame(String title, int width, int height, Component component, LayoutManager layout) {
        super.setTitle(title);
        super.setSize(width, height);
        super.setLocationRelativeTo(component);
        super.setLayout(layout);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void init() {
        this.add(content);
    }
}
