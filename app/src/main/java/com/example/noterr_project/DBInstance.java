package com.example.noterr_project;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBInstance {
    private static SQLiteDatabase db;

    private DBInstance() {}

    public static void init(Context context) {
        if (db != null) return;
        String dbName = "noterr_db";
        String dbPath = context.getFilesDir().getPath() + "/" + dbName;
        db = openOrCreateDatabase(dbPath, null);

        // Create config table, not used
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

        // Create reminders table (FIXED TABLE NAME)
        StringBuilder createRemindersTableQuery = new StringBuilder();
        createRemindersTableQuery.append("CREATE TABLE IF NOT EXISTS reminders(");
        createRemindersTableQuery.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createRemindersTableQuery.append("title VARCHAR, ");
        createRemindersTableQuery.append("content VARCHAR, ");
        createRemindersTableQuery.append("time VARCHAR, ");
        createRemindersTableQuery.append("completed INTEGER DEFAULT 0, ");
        createRemindersTableQuery.append("created_on DATETIME DEFAULT CURRENT_TIMESTAMP, ");
        createRemindersTableQuery.append("modified_on DATETIME DEFAULT CURRENT_TIMESTAMP");
        createRemindersTableQuery.append(")");
        db.execSQL(createRemindersTableQuery.toString());

        // Create trigger to update 'modified_on' for reminders table (FIXED TABLE NAME)
        StringBuilder createRemindersTriggerQuery = new StringBuilder();
        createRemindersTriggerQuery.append("CREATE TRIGGER IF NOT EXISTS update_modified_on_reminders ");
        createRemindersTriggerQuery.append("AFTER UPDATE ON reminders ");
        createRemindersTriggerQuery.append("FOR EACH ROW ");
        createRemindersTriggerQuery.append("BEGIN ");
        createRemindersTriggerQuery.append("UPDATE reminders SET modified_on = CURRENT_TIMESTAMP WHERE id = OLD.id; ");
        createRemindersTriggerQuery.append("END;");
        db.execSQL(createRemindersTriggerQuery.toString());
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
