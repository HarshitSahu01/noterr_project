package com.example.noterr_project;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.database.sqlite.SQLiteDatabase;

public class DBInstance {
    private SQLiteDatabase db;
    private DBInstance() {}
    SQLiteDatabase getInstance() {
        if (db == null) {
            db = openOrCreateDatabase("noterr_db", null);
            StringBuilder createConfigQuery = new StringBuilder();
            createConfigQuery.append("CREATE TABLE IF NOT EXISTS config(");
            createConfigQuery.append("private_password varchar");
            createConfigQuery.append(")");
            db.execSQL(createConfigQuery.toString());

            StringBuilder createNotesTableQuery = new StringBuilder();
            createNotesTableQuery.append("CREATE TABLE IF NOT EXISTS notes(");
            createNotesTableQuery.append("private_password varchar");
            createNotesTableQuery.append(")");
            db.execSQL(createNotesTableQuery.toString());

            StringBuilder createRemaindersTableQuery = new StringBuilder();
            createRemaindersTableQuery.append("CREATE TABLE IF NOT EXISTS config(");
            createRemaindersTableQuery.append("private_password varchar");
            createRemaindersTableQuery.append(")");
            db.execSQL(createRemaindersTableQuery.toString());

        }
        return db;
    }
    public void closeInstance() {
        if (db == null) return;
        db.close();
        db = null;
    }
}
