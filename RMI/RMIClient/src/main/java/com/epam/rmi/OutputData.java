package com.epam.rmi;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OutputData implements Serializable {
    private String text;
    private LocalDateTime localDateTime;

    public OutputData(String text, LocalDateTime localDateTime) {
        this.text = text;
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}