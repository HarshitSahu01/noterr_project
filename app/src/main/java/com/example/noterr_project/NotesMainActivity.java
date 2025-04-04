package com.example.noterr_project;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesMainActivity extends AppCompatActivity {

    private LinearLayout notesContainer;
    private TextView notesCount;
    private TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);

        headerText = findViewById(R.id.headerText);
        headerText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back, 0, 0, 0);
        headerText.setOnClickListener(v -> onBackPressed());

        notesContainer = findViewById(R.id.notesContainer);
        notesCount = findViewById(R.id.notescount);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(NotesMainActivity.this, NotesEditorActivity.class)));

        ImageButton btnMoreOptions = findViewById(R.id.btnMoreOptions);
        btnMoreOptions.setOnClickListener(this::showPopupMenu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        notesContainer.removeAllViews();

        Note[] notes = Note.getNotes();
        notesCount.setText(notes.length + " Notes");

        for (Note note : notes) {
            View noteCard = createCard(note.id, note.title, note.content, note.modified_on);
            notesContainer.addView(noteCard);
        }
    }

    private View createCard(final int noteId, String title, String content, String timestamp) {

        CardView cardView = new CardView(this);


        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(100)
        );
        cardParams.setMargins(0, dpToPx(12), 0, 0);
        cardView.setLayoutParams(cardParams);


        cardView.setCardElevation(dpToPx(6));
        cardView.setRadius(dpToPx(12));
        cardView.setCardBackgroundColor(Color.parseColor("#3E1E68"));


        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        relativeLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));


        TextView titleText = new TextView(this);
        titleText.setId(View.generateViewId());
        titleText.setText(title);
        titleText.setTextSize(18);
        titleText.setTextColor(Color.WHITE);

        TextView contentText = new TextView(this);
        contentText.setText(content);
        contentText.setTextSize(14);
        contentText.setTextColor(Color.WHITE);

        TextView timestampText = new TextView(this);
        timestampText.setText(timestamp);
        timestampText.setTextSize(12);
        timestampText.setTextColor(Color.parseColor("#B39DDB"));


        ImageView deleteButton = new ImageView(this);
        deleteButton.setImageResource(R.drawable.ic_delete);
        deleteButton.setColorFilter(Color.WHITE);
        deleteButton.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));


        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        titleText.setLayoutParams(titleParams);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        contentParams.addRule(RelativeLayout.BELOW, titleText.getId());
        contentText.setLayoutParams(contentParams);

        RelativeLayout.LayoutParams timestampParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        timestampParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        timestampParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        timestampText.setLayoutParams(timestampParams);

        RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(
                dpToPx(36), dpToPx(36)
        );
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        deleteButton.setLayoutParams(deleteParams);


        relativeLayout.addView(titleText);
        relativeLayout.addView(contentText);
        relativeLayout.addView(timestampText);
        relativeLayout.addView(deleteButton);
        cardView.addView(relativeLayout);


        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(NotesMainActivity.this, NotesEditorActivity.class);
            intent.putExtra("noteId", noteId);
            startActivity(intent);
        });


        deleteButton.setOnClickListener(v -> showDeleteConfirmation(noteId));

        return cardView;
    }


    private void showDeleteConfirmation(final int noteId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Note.deleteNote(noteId);
                    loadNotes();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_notes_main, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_private_notes) {
                Intent intent = new Intent(NotesMainActivity.this, LockPageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
