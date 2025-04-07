package com.example.noterr_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LockPageActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LockPrefs";
    private static final String PREF_PASSWORD = "app_password";
    private static final String DEFAULT_PASSWORD = "12345"; // Default password

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_page);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Get the stored password or use default if not set
        final String correctPassword = sharedPreferences.getString(PREF_PASSWORD, DEFAULT_PASSWORD);

        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonUnlock = findViewById(R.id.buttonUnlock);
        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        buttonUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = editTextPassword.getText().toString();
                if (enteredPassword.equals(correctPassword)) {
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
                // Reset to default password when forgot password is clicked
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_PASSWORD, DEFAULT_PASSWORD);
                editor.apply();
                Toast.makeText(LockPageActivity.this, "Password reset to default. New password: " + DEFAULT_PASSWORD, Toast.LENGTH_LONG).show();
            }
        });
    }
}