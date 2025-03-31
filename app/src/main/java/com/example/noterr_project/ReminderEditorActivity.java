package com.example.noterr_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.Locale;

public class ReminderEditorActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, dateEditText, timeEditText;
    private Reminder currentReminder;
    private static final String DATE_FORMAT = "%02d/%02d/%04d";
    private static final String TIME_FORMAT = "%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);

        initializeToolbar();
        initializeViews();
        setupReminderData();
        setupDateTimePickers();
        setupSaveButton();
    }

    private void initializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("New Reminder"); // Default title
        }
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        dateEditText = findViewById(R.id.editTextDate);
        timeEditText = findViewById(R.id.editTextTime);
    }

    private void setupReminderData() {
        int reminderId = getIntent().getIntExtra("REMINDER_ID", -1);

        if (reminderId != -1) {
            currentReminder = Reminder.getReminder(reminderId);
            if (currentReminder != null) {
                populateReminderData();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Edit Reminder");
                }
            }
        } else {
            currentReminder = new Reminder();
            setDefaultDateTime();
        }
    }

    private void populateReminderData() {
        titleEditText.setText(currentReminder.title);
        descriptionEditText.setText(currentReminder.content);

        if (currentReminder.time != null && !currentReminder.time.isEmpty()) {
            String[] dateTimeParts = currentReminder.time.split(" ");
            if (dateTimeParts.length >= 2) {
                dateEditText.setText(dateTimeParts[0]);
                timeEditText.setText(dateTimeParts[1]);
            }
        }
    }

    private void setDefaultDateTime() {
        Calendar calendar = Calendar.getInstance();
        dateEditText.setText(String.format(Locale.getDefault(), DATE_FORMAT,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)));
        timeEditText.setText(String.format(Locale.getDefault(), TIME_FORMAT,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)));
    }

    private void setupDateTimePickers() {
        dateEditText.setOnClickListener(v -> showDatePicker());
        timeEditText.setOnClickListener(v -> showTimePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> dateEditText.setText(
                        String.format(Locale.getDefault(), DATE_FORMAT, day, month + 1, year)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this,
                (view, hour, minute) -> timeEditText.setText(
                        String.format(Locale.getDefault(), TIME_FORMAT, hour, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true)
                .show();
    }

    private void setupSaveButton() {
        MaterialButton saveButton = findViewById(R.id.btnAddTask);
        saveButton.setOnClickListener(v -> {
            if (validateInput()) {
                saveReminder();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean validateInput() {
        if (titleEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dateEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (timeEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveReminder() {
        String title = titleEditText.getText().toString().trim();
        String content = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        currentReminder.setTitle(title);
        currentReminder.setContent(content);
        currentReminder.setTime(date + " " + time);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}