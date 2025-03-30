package com.example.noterr_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Note {
    int id;
    String title="";
    String content="";
    int is_private=0;
    String created_on;
    String modified_on;
    SQLiteDatabase db;

    private static Note[] notes = {
            new Note(1, "Title 1", "Content 1", 0, "", ""),
            new Note(2, "Title 2", "Content 2", 0, "", ""),
            new Note(3, "Title 3", "Content 3", 0, "", ""),
    };

    public Note(){
        db.execSQL("insert into notes (title, content) values ('', '')");
        try (
                Cursor c = db.rawQuery("select id, created_on, modified_on from notes where id = (select max(id) from notes)", null)
                ) {
            id = c.getInt(1);
            created_on = c.getString(2);
            modified_on = c.getString(3);
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
        db.execSQL("update notes set title=? where id = ?", new String[]{title, Integer.toString(id)});
    }

    public void setContent(String content) {
        this.content = content.replaceAll("(['\"\\\\])", "\\\\$1");
        db.execSQL("update notes set content=? where id = ?", new String[]{content, Integer.toString(id)});
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
        db.execSQL("update notes set is_private=? where id = ?", new String[]{Integer.toString(is_private), Integer.toString(id)});
    }

    public static Note[] getNotes() {
        SQLiteDatabase db = DBInstance.getInstance();
        Cursor c = db.rawQuery("SELECT * FROM NOTES", null);
        Note[] notes = new Note[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            notes[i++] = new Note(c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
        }
        c.close();
        return notes;
    }

    public static Note getNote(int id) {
        SQLiteDatabase db = DBInstance.getInstance();
        try (
                Cursor c = db.rawQuery("SELECT * FROM NOTES WHERE id = ?", new String[]{Integer.toString(id)})
                ) {
            if (!c.moveToNext()) return null;
            return new Note(c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
        }
    }
    public int delete() {
        return db.delete("notes", "id = ?", new String[]{Integer.toString(id)});
    }
    public void save() {

    }
}