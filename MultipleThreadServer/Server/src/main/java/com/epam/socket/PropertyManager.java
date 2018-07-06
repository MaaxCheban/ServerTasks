package com.epam.socket;

import java.io.*;
import java.util.Properties;

public class PropertyManager {
    private final String propertyFileName;
    private FileInputStream in;
    private FileOutputStream out;
    private Properties properties;

    public PropertyManager(String propertyFileName){

        this.propertyFileName = propertyFileName;
        properties = new Properties();

    }

    public InputStream getInputStream(){
        return in;
    }

    public FileOutputStream getOutputStream(){
        return out;
    }

    public synchronized String getProperty(String key){
        try {
            in = new FileInputStream(propertyFileName);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        String result = properties.getProperty(key);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return result;
    }

    public synchronized void setProperty(String key, String val){
        try {
            out = new FileOutputStream(propertyFileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        properties.setProperty(key, val);

        try {
            properties.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
