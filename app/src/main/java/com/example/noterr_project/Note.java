package com.example.noterr_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Note {
    int id;
    String title = "";
    String content = "";
    int is_private = 0;
    String created_on;
    String modified_on;
    SQLiteDatabase db;

    public Note() {
        db = DBInstance.getInstance();
        db.execSQL("INSERT INTO notes (title, content) VALUES ('', '')");
        try (
                Cursor c = db.rawQuery("SELECT id, created_on, modified_on FROM notes WHERE id = (SELECT MAX(id) FROM notes)", null)
        ) {
            if (c.moveToFirst()) {
                id = c.getInt(0);
                created_on = c.getString(1);
                modified_on = c.getString(2);
            }
        }
    }

    private Note(int id, String title, String content, int is_private, String created_on, String modified_on) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.is_private = is_private;
        this.created_on = created_on;
        this.modified_on = modified_on;
        db = DBInstance.getInstance();
    }

    public void setTitle(String title) {
        this.title = title.replaceAll("(['\"\\\\])", "\\\\$1");
        db.execSQL("UPDATE notes SET title=? WHERE id = ?", new Object[]{title, id});
    }

    public void setContent(String content) {
        this.content = content.replaceAll("(['\"\\\\])", "\\\\$1");
        db.execSQL("UPDATE notes SET content=? WHERE id = ?", new Object[]{content, id});
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
        db.execSQL("UPDATE notes SET private=? WHERE id = ?", new Object[]{is_private, id});
    }

    public static Note[] getNotes() {
        SQLiteDatabase db = DBInstance.getInstance();
        try (
                Cursor c = db.rawQuery("SELECT * FROM NOTES WHERE private = 0 ORDER BY modified_on DESC", null)
        ) {
            Note[] notes = new Note[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                notes[i++] = new Note(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5));
            }
            c.close();
            return notes;
        }
    }

    public static Note[] getPrivateNotes() {
        SQLiteDatabase db = DBInstance.getInstance();
        try (
                Cursor c = db.rawQuery("SELECT * FROM NOTES WHERE private = 1 ORDER BY modified_on DESC", null)
        ) {
            Note[] notes = new Note[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                notes[i++] = new Note(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5));
            }
            c.close();
            return notes;
        }
    }

    public static Note getNote(int id) {
        SQLiteDatabase db = DBInstance.getInstance();
        try (
                Cursor c = db.rawQuery("SELECT * FROM NOTES WHERE id = ?", new String[]{Integer.toString(id)})
        ) {
            if (!c.moveToFirst()) return null;
            return new Note(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5));
        }
    }

    public int delete() {
        return db.delete("notes", "id = ?", new String[]{Integer.toString(id)});
    }

    public static int deleteNote(int noteId) {
        SQLiteDatabase db = DBInstance.getInstance();
        return db.delete("notes", "id = ?", new String[]{String.valueOf(noteId)});
    }
}
