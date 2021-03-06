package com.epam.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteFileWriter extends Remote {
    void write(OutputData outputData, long id) throws RemoteException;

    long init() throws RemoteException;
}