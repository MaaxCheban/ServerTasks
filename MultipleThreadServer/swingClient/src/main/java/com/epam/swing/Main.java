package com.epam.swing;


import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        // write your code here
        try {
            SwingUtilities.invokeAndWait(new Runnable(){
                public void run(){
                    Window window = new Window();
                    Content content = new Content.ContentBuilder().build();
                    window.setContent(content);
                    window.init();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }
}

