package com.epam.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface RemoteFileWriter extends Remote {
    void write(String text, LocalDateTime localDateTime, long id) throws RemoteException;
}
