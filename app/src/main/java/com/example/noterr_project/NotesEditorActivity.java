package com.example.noterr_project;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NotesEditorActivity extends AppCompatActivity {

    private EditText noteTitle, noteEditText;
    private Note currentNote;
    private TextView headerText;

    private String originalTitle = "";
    private String originalContent = "";
    private boolean isNewNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        headerText = findViewById(R.id.headerText);
        headerText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back, 0, 0, 0);
        headerText.setOnClickListener(v -> onBackPressed());

        noteTitle = findViewById(R.id.noteTitle);
        noteEditText = findViewById(R.id.noteEditText);

        int noteId = getIntent().getIntExtra("noteId", -1);
        int is_private = getIntent().getIntExtra("private", 0);

        if (noteId == -1) {
            currentNote = new Note();
            currentNote.setIs_private(is_private);
            isNewNote = true;
        } else {
            currentNote = Note.getNote(noteId);
            if (currentNote != null) {
                noteTitle.setText(currentNote.title);
                noteEditText.setText(currentNote.content);
                originalTitle = currentNote.title != null ? currentNote.title : "";
                originalContent = currentNote.content != null ? currentNote.content : "";
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        String updatedTitle = noteTitle.getText().toString().trim();
        String updatedContent = noteEditText.getText().toString().trim();

        if (updatedTitle.isEmpty() && updatedContent.isEmpty()) {
            currentNote.delete();
            return;
        }

        // Save only if changes occurred
        if (!updatedTitle.equals(originalTitle) || !updatedContent.equals(originalContent)) {
            currentNote.setTitle(updatedTitle);
            currentNote.setContent(updatedContent);
        }
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
