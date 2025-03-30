package com.example.noterr_project;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBInstance {
    private static SQLiteDatabase db;

    private DBInstance() {}
    public static void initialize(Context context) {
        if (db != null) return;
        String dbName = "noterr_db";
        String dbPath = context.getFilesDir().getPath()+"/"+dbName;
        db = openOrCreateDatabase(dbPath, null);
        // Create config table
        StringBuilder createConfigQuery = new StringBuilder();
        createConfigQuery.append("CREATE TABLE IF NOT EXISTS config(");
        createConfigQuery.append("key VARCHAR PRIMARY KEY, ");
        createConfigQuery.append("value VARCHAR");
        createConfigQuery.append(")");
        db.execSQL(createConfigQuery.toString());

        // Create notes table
        StringBuilder createNotesTableQuery = new StringBuilder();
        createNotesTableQuery.append("CREATE TABLE IF NOT EXISTS notes(");
        createNotesTableQuery.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createNotesTableQuery.append("title VARCHAR, ");
        createNotesTableQuery.append("content VARCHAR, ");
        createNotesTableQuery.append("private INTEGER DEFAULT 0, ");
        createNotesTableQuery.append("created_on DATETIME DEFAULT CURRENT_TIMESTAMP, ");
        createNotesTableQuery.append("modified_on DATETIME DEFAULT CURRENT_TIMESTAMP");
        createNotesTableQuery.append(")");
        db.execSQL(createNotesTableQuery.toString());

        // Create trigger to update 'modified_on' for notes table
        StringBuilder createNotesTriggerQuery = new StringBuilder();
        createNotesTriggerQuery.append("CREATE TRIGGER IF NOT EXISTS update_modified_on_notes ");
        createNotesTriggerQuery.append("AFTER UPDATE ON notes ");
        createNotesTriggerQuery.append("FOR EACH ROW ");
        createNotesTriggerQuery.append("BEGIN ");
        createNotesTriggerQuery.append("UPDATE notes SET modified_on = CURRENT_TIMESTAMP WHERE id = OLD.id; ");
        createNotesTriggerQuery.append("END;");
        db.execSQL(createNotesTriggerQuery.toString());

        // Create remainder table
        StringBuilder createRemaindersTableQuery = new StringBuilder();
        createRemaindersTableQuery.append("CREATE TABLE IF NOT EXISTS remainder(");
        createRemaindersTableQuery.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createRemaindersTableQuery.append("title VARCHAR, ");
        createRemaindersTableQuery.append("content VARCHAR, ");
        createRemaindersTableQuery.append("created_on DATETIME DEFAULT CURRENT_TIMESTAMP, ");
        createRemaindersTableQuery.append("modified_on DATETIME DEFAULT CURRENT_TIMESTAMP");
        createRemaindersTableQuery.append(")");
        db.execSQL(createRemaindersTableQuery.toString());

        // Create trigger to update 'modified_on' for remainder table
        StringBuilder createRemaindersTriggerQuery = new StringBuilder();
        createRemaindersTriggerQuery.append("CREATE TRIGGER IF NOT EXISTS update_modified_on_remainder ");
        createRemaindersTriggerQuery.append("AFTER UPDATE ON remainder ");
        createRemaindersTriggerQuery.append("FOR EACH ROW ");
        createRemaindersTriggerQuery.append("BEGIN ");
        createRemaindersTriggerQuery.append("UPDATE remainder SET modified_on = CURRENT_TIMESTAMP WHERE id = OLD.id; ");
        createRemaindersTriggerQuery.append("END;");
        db.execSQL(createRemaindersTriggerQuery.toString());
    }

    static SQLiteDatabase getInstance() {
        return db;
    }

    public void closeInstance() {
        if (db == null) return;
        db.close();
        db = null;
    }
}
