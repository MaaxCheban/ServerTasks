package com.epam.rmi;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import static java.time.temporal.ChronoUnit.MINUTES;

public class ActiveUsersScannerTask implements Runnable {
    private final ConcurrentMap<Long, LocalTime> activeUsers;
    private final Executor executor;
    private boolean mustRun = false;

    public ActiveUsersScannerTask( ConcurrentMap<Long, LocalTime> usersConnectTime){
        this.activeUsers = usersConnectTime;
        executor = Executors.newFixedThreadPool(1);
    }

    public void start(){
        executor.execute(this);
    }

    public void stop(){
        mustRun = false;
    }

    @Override
    public void run() {
        mustRun = true;
        while(mustRun){
            activeUsers.entrySet().removeIf(entry -> MINUTES.between(entry.getValue(), LocalTime.now()) > 5);
        }
    }
}
