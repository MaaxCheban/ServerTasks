package com.epam.socket;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public synchronized void addValueToList(String key, String val) throws Hacker_Exception {
        List<String> values;

        if(getProperty(key).length() == 0){
            setProperty(key, val);
            return;
        }else{
            values = new ArrayList<>(Arrays.asList(getProperty(key).split(",")));
        }

        if(values.indexOf(val) != - 1){
            throw new Hacker_Exception();
        }

        values.add(val);
        if(values.size() == 1){
            setProperty(key, values.get(0));
        }else{
            setProperty(key, String.join(",", values));
        }
    }

    public synchronized void removeValueFromList(String key, String val){
        List<String> values = new ArrayList<>(Arrays.asList(getProperty(key).split(",")));
        values.remove(val);
        setProperty(key, String.join(",", values));
    }

}
