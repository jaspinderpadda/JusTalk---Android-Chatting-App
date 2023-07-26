package com.jspadda.justalk.Models;

public class Status {
    private String imagineURL;
    private long timestamp;

    public Status() {
    }

    public String getImagineURL() {
        return imagineURL;
    }

    public void setImagineURL(String imagineURL) {
        this.imagineURL = imagineURL;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Status(String imagineURL, long timestamp) {
        this.imagineURL = imagineURL;
        this.timestamp = timestamp;


    }
}
