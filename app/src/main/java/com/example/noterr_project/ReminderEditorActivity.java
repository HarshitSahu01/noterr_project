package com.example.noterr_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Calendar;
import java.util.Locale;
import android.text.Editable;
import android.text.TextWatcher;

public class ReminderEditorActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, dateEditText, timeEditText;
    private Reminder currentReminder;
    private static final String DATE_FORMAT = "%02d/%02d/%04d";
    private static final String TIME_FORMAT = "%02d:%02d";
    private Button btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("New Reminder");
        }

        initializeViews();
        setupReminderData();
        setupDateTimePickers();
        addTextWatchers();
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
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        dateEditText = findViewById(R.id.editTextDate);
        timeEditText = findViewById(R.id.editTextTime);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(v -> onBackPressed());
    }

    private void setupReminderData() {
        int reminderId = getIntent().getIntExtra("reminderId", -1);
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
        if (currentReminder.time != null) {
            String[] parts = currentReminder.time.split(" ");
            if (parts.length >= 2) {
                dateEditText.setText(parts[0]);
                timeEditText.setText(parts[1]);
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
                (view, year, month, day) -> {
                    dateEditText.setText(String.format(Locale.getDefault(), DATE_FORMAT, day, month + 1, year));
                    saveReminder();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this,
                (view, hour, minute) -> {
                    timeEditText.setText(String.format(Locale.getDefault(), TIME_FORMAT, hour, minute));
                    saveReminder();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true)
                .show();
    }

    private void addTextWatchers() {
        titleEditText.addTextChangedListener(reminderTextWatcher);
        descriptionEditText.addTextChangedListener(reminderTextWatcher);
        dateEditText.addTextChangedListener(reminderTextWatcher);
        timeEditText.addTextChangedListener(reminderTextWatcher);
    }

    private final TextWatcher reminderTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            saveReminder();
        }
    };

    private void saveReminder() {
        if (titleEditText.getText().toString().trim().isEmpty()) {
            return;
        }
        currentReminder.setTitle(titleEditText.getText().toString().trim());
        currentReminder.setContent(descriptionEditText.getText().toString().trim());
        currentReminder.setTime(dateEditText.getText().toString() + " " + timeEditText.getText().toString());
    }
    // Adding the current reminder to the list of reminders
    protected void onPause(Bundle savedInstanceState) {
        super.onPause();
        saveReminder();
        ReminderScheduler.scheduleAlarm(currentReminder.id, currentReminder.title, currentReminder.content, currentReminder.time);
    }
}
