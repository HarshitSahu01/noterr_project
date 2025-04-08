package com.example.noterr_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private static final String PREF_FIRST_LOGIN = "first_login";
    private static String DEFAULT_PASSWORD = ""; // Default password

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_page);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        boolean isFirstLogin = sharedPreferences.getBoolean(PREF_FIRST_LOGIN, true);

        if (isFirstLogin) {
            createPassword();
            return;
        }

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
                    finish();
                } else {
                    Toast.makeText(LockPageActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_FIRST_LOGIN, true);
                editor.apply();

                Toast.makeText(LockPageActivity.this, "Please set a new password", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
    }

    private void createPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Create Password");
        alert.setMessage("Please set a password for your private notes");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setCancelable(false);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = String.valueOf(input.getText()).trim();
                if (value.isEmpty()) {
                    Toast.makeText(LockPageActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockPageActivity.this, NotesMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREF_PASSWORD, value);
                    editor.putBoolean(PREF_FIRST_LOGIN, false); // Mark first login as completed
                    editor.apply();

                    Toast.makeText(LockPageActivity.this, "Password created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockPageActivity.this, PrivateNotesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(LockPageActivity.this, NotesMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alert.show();
    }
}