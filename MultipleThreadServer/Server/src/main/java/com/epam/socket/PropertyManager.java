package com.epam.socket;

import java.io.*;
import java.util.*;

public class PropertyManager {
    private final String propertyFileName;
    private final Properties properties;

    public PropertyManager(String propertyFileName) {
        this.propertyFileName = propertyFileName;

        File propFile = new File(propertyFileName);

        properties = new Properties();
        if (!propFile.exists()) {
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (getProperty("counter") == null) {
            setProperty("counter", "0");
        }

        setProperty("active_users", "");
    }

    public synchronized String getProperty(String key) {
        try (FileInputStream in = new FileInputStream(propertyFileName)) {
            properties.load(in);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setProperty(String key, String val) {
        try (FileOutputStream out = new FileOutputStream(propertyFileName)) {
            properties.setProperty(key, val);
            properties.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public synchronized void addValueToList(String key, String val) throws HackerException {
        List<String> values;

        if (getProperty(key).isEmpty()) {
            setProperty(key, val);
            return;
        }

        values = new ArrayList<>(Arrays.asList(getProperty(key).split(",")));

        if (values.contains(val)) {
            throw new HackerException();
        }

        values.add(val);

        setProperty(key, String.join(",", values));
    }

    public synchronized void removeValueFromList(String key, String val) {
        if (getProperty(key) == null) {
            throw new IllegalArgumentException("There is no key:" + key);
        }

        List<String> values = new ArrayList<>(Arrays.asList(getProperty(key).split(",")));
        values.remove(val);
        setProperty(key, String.join(",", values));
    }
}
