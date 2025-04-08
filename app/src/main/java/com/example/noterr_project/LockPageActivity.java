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
    private static String DEFAULT_PASSWORD = ""; // Default password

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_page);


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        // Get the stored password or use default if not set
       String password = sharedPreferences.getString(PREF_PASSWORD, DEFAULT_PASSWORD);
        if(password.isEmpty()){
            createPassword();
            password = sharedPreferences.getString(PREF_PASSWORD, DEFAULT_PASSWORD);
        }
        final String correctPassword = password;

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
        private void createPassword(){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Title");
            alert.setMessage("Message");

// Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = String.valueOf(input.getText());
                    Toast.makeText(LockPageActivity.this,value,Toast.LENGTH_LONG);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }

}