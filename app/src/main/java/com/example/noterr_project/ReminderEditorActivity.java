package com.example.noterr_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class ReminderEditorActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextEvent, editTextDate, editTextTime;
    private MaterialButton btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);

        // Initialize UI Elements
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextEvent = findViewById(R.id.editTextEvent);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        btnAddTask = findViewById(R.id.btnAddTask);

        // Date Picker
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(ReminderEditorActivity.this, (view, year1, month1, dayOfMonth) ->
                        editTextDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                        year, month, day).show();
            }
        });

        // Time Picker
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                new TimePickerDialog(ReminderEditorActivity.this, (view, hourOfDay, minute1) ->
                        editTextTime.setText(hourOfDay + ":" + minute1), hour, minute, true).show();
            }
        });

        // Add Reminder Action
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(ReminderEditorActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReminderEditorActivity.this, "Reminder Added!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
