package com.example.noterr_project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotesEditorActivity extends AppCompatActivity {
    private EditText noteTitle, noteEditText;
    private Note currentNote;
    private TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        headerText = findViewById(R.id.headerText);
        headerText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back, 0, 0, 0);
        headerText.setOnClickListener(v -> onBackPressed());

        noteTitle = findViewById(R.id.noteTitle);
        noteEditText = findViewById(R.id.noteEditText);

        // Check if an existing note is being edited
        int noteId = getIntent().getIntExtra("noteId", -1);
        int is_private = getIntent().getIntExtra("private", 0);
        if (noteId == -1) {
            // Create a new note
            currentNote = new Note();
            currentNote.setIs_private(is_private);
        } else {
            // Load existing note
            currentNote = Note.getNote(noteId);
            noteTitle.setText(currentNote.title);
            noteEditText.setText(currentNote.content);
        }

        // Add auto-save when text changes
        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNote.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}