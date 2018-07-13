package com.epam.rmi;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OutputData implements Serializable {
    private final String text;
    private final LocalDateTime localDateTime;

    public OutputData(String text, LocalDateTime localDateTime) {
        this.text = text;
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public String getText() {
        return text;
    }

}