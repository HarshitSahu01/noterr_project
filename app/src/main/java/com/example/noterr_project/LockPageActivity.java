package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LockPageActivity extends AppCompatActivity {

    private static final String CORRECT_PASSWORD = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_page);

        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonUnlock = findViewById(R.id.buttonUnlock);
        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        buttonUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = editTextPassword.getText().toString();
                if (enteredPassword.equals(CORRECT_PASSWORD)) {
                    Toast.makeText(LockPageActivity.this, "Unlocked Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockPageActivity.this, PrivateNotesActivity.class);
                    startActivity(intent);
                    finish(); // Close the lock page
                } else {
                    Toast.makeText(LockPageActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LockPageActivity.this, "Reset instructions sent to your email.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}