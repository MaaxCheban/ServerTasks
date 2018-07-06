package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface RemoteFileWriter extends Remote {
    void write(String text) throws RemoteException;
}