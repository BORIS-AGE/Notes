package com.example.boris.notes.models;

public class NoteItem {
    long date;
    String body;

    public NoteItem(long date, String body) {
        this.date = date;
        this.body = body;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
