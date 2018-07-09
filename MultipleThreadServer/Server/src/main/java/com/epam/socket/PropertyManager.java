package com.epam.socket;

import java.io.*;
import java.util.*;

public class PropertyManager {
    private final String propertyFileName;
    private final Properties properties;

    public PropertyManager(String propertyFileName){
        this.propertyFileName = propertyFileName;

        File propFile = new File(propertyFileName);

        properties = new Properties();
        if(propFile.exists() && !propFile.isDirectory()){
            if(getProperty("counter") == null){
                setProperty("counter", "0");
            }

            if(getProperty("active_users") == null){
                setProperty("active_users", "");
            }else{
                setProperty("active_users", "");
            }

        }else{
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setProperty("active_users", "");
            setProperty("counter", "0");
        }
    }

    public synchronized String getProperty(String key){
        try(FileInputStream in = new FileInputStream(propertyFileName)){
            properties.load(in);
            return properties.getProperty(key);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setProperty(String key, String val){
        try(FileOutputStream out = new FileOutputStream(propertyFileName)){
            properties.setProperty(key, val);
            properties.store(out, null);
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public synchronized void addValueToList(String key, String val) throws Hacker_Exception {
        List<String> values;

        if(getProperty(key).isEmpty()){
            setProperty(key, val);
            return;
        }

        values = new ArrayList<>(Arrays.asList(getProperty(key).split(",")));

        if(values.contains(val)){
            throw new Hacker_Exception();
        }

        values.add(val);


        setProperty(key, String.join(",", values));
    }

    public synchronized void removeProperty(String key, String val) {
        List<String> values = new ArrayList<>(Arrays.asList(getProperty(key).split(",")));
        values.remove(val);
        setProperty(key, String.join(",", values));
    }
}
