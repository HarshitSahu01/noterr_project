package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesMainActivity extends AppCompatActivity {

    private LinearLayout notesContainer;
    private TextView notesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);

        notesContainer = findViewById(R.id.notesContainer);
        notesCount = findViewById(R.id.notescount);

        // Load and display notes
        loadNotes();

        // Floating Action Button to add new notes
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesMainActivity.this, NotesEditorActivity.class));
            }
        });

        // More Options button for dropdown menu
        ImageButton btnMoreOptions = findViewById(R.id.btnMoreOptions);
        btnMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);  // Show dropdown menu
            }
        });
    }

    // Reload notes when returning to this activity
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();  // Refresh notes list
    }

    private void loadNotes() {
        notesContainer.removeAllViews(); // Clear existing notes

        Note[] notes = Note.getNotes();
        notesCount.setText(notes.length + " Notes"); // Update notes count

        for (Note note : notes) {
            View noteCard = createCard(note.title, note.content, note.modified_on);
            notesContainer.addView(noteCard);
        }
    }

    // Create a card view dynamically for each note
    private View createCard(String title, String content, String timestamp) {
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        cardView.setCardElevation(8);
        cardView.setRadius(16);
        cardView.setPadding(16, 16, 16, 16);
        cardView.setUseCompatPadding(true);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.cardBackground));

        // Container for text elements inside the card
        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(16, 16, 16, 16);

        // Title TextView
        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(20);
        titleText.setTextColor(getResources().getColor(R.color.white));

        // Content TextView
        TextView contentText = new TextView(this);
        contentText.setText(content);
        contentText.setTextSize(16);
        contentText.setTextColor(getResources().getColor(R.color.gray));

        // Timestamp TextView
        TextView timestampText = new TextView(this);
        timestampText.setText(timestamp);
        timestampText.setTextSize(12);
        timestampText.setTextColor(getResources().getColor(R.color.timestampColor));

        // Add text elements to card
        cardContent.addView(titleText);
        cardContent.addView(contentText);
        cardContent.addView(timestampText);
        cardView.addView(cardContent);

        // Open editor when clicking on a note
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesMainActivity.this, NotesEditorActivity.class);
                intent.putExtra("noteId", Note.getNoteId(title)); // Get ID by title (or another method)
                startActivity(intent);
            }
        });

        return cardView;
    }

    // Dropdown menu for extra options
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_notes_main, popupMenu.getMenu());

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_private_notes) {
                    Intent intent = new Intent(NotesMainActivity.this, LockPageActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();  // Show the dropdown menu
    }
}
