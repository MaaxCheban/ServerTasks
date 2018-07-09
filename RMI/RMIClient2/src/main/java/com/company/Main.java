package com.company;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main( String[] args )
    {
        try {
            SwingUtilities.invokeAndWait(() -> {
                ConnectPanel connectPanel= new ConnectPanel.ConnectPanelBuilder().build();
                CommunicationPanel communicationPanel= new CommunicationPanel.CommunicationPanelBuilder().build();

                Content content = new Content(connectPanel, communicationPanel);
                Window window = new Window(content);
                window.setVisible(true);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
