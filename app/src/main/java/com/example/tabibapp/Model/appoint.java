package com.example.tabibapp.Model;

import java.util.Date;

public class appoint {
    private String from;
    private String day;
    private String to;


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public appoint(String from, String day, String to) {
        this.from = from;
        this.day = day;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public appoint() {
    }
}
