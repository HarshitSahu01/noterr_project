package com.example.noterr_project;

public class Remainder {
    int id;
    String title;
    String content;
    String time;
    String created_on;
    String modified_on;

    private static Remainder[] remainders = {
            new Remainder(1, "Remainder 1", "Description 1", "10:00 AM", "", ""),
            new Remainder(2, "Remainder 2", "Description 2", "2:00 PM", "", ""),
            new Remainder(3, "Remainder 3", "Description 3", "5:30 PM", "", "")
    };

    public Remainder(int id, String title, String content, String time, String created_on, String modified_on) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.created_on = created_on;
        this.modified_on = modified_on;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static Remainder[] getRemainders() {
        return remainders;
    }

    public static Remainder getRemainder(int id) {
        return remainders[0];
    }
    public void save(){}
}
