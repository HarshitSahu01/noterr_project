package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find Button & Set Click Listener
        Button buttonOpenNotes = findViewById(R.id.buttonOpenNotes);
        buttonOpenNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open NotesMainActivity
                Intent intent = new Intent(MainActivity.this, NotesMainActivity.class);
                startActivity(intent);
            }
        });
    }
}

