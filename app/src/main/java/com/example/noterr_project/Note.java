package com.example.noterr_project;

public class Note {
    int id;
    String title;
    String content;
    int is_private;
    String created_on;
    String modified_on;

    private static Note[] notes = {
            new Note(1, "Title 1", "Content 1", 0, "", ""),
            new Note(2, "Title 2", "Content 2", 0, "", ""),
            new Note(3, "Title 3", "Content 3", 0, "", ""),
    };

    public Note(int id, String title, String content, int is_private, String created_on, String modified_on) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.is_private = is_private;
        this.created_on = created_on;
        this.modified_on = modified_on;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Note[] getNotes() {
        return notes;
    }

    public static Note getNote(int id) {
        return notes[0];
    }
}