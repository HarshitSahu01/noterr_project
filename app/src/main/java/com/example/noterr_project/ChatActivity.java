package com.example.noterr_project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private TextView responseTextView;
    private TextView userInputTextView;
    private CardView userInputCard;
    private EditText promptEditText;
    private FloatingActionButton sendButton;
    private ProgressBar progressBar;
    private OkHttpClient client;
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String API_KEY = "AIzaSyCLHke-0z-NA6JNysupMvsk7nuIXWfTyAA"; // Replace with your actual Gemini API key
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        responseTextView = findViewById(R.id.responseTextView);
        userInputTextView = findViewById(R.id.userInputTextView);
        userInputCard = findViewById(R.id.userInputCard);
        promptEditText = findViewById(R.id.promptEditText);
        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);

        client = new OkHttpClient();

        sendButton.setOnClickListener(v -> {
            String userPrompt = promptEditText.getText().toString().trim();
            if (!userPrompt.isEmpty()) {
                sendPromptToGemini(userPrompt);
                promptEditText.setText("");
            }
        });
    }

    private void sendPromptToGemini(String userPrompt) {
        userInputTextView.setText(userPrompt);
        userInputTextView.setVisibility(View.VISIBLE);
        userInputCard.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        responseTextView.setText("Processing your request...");

        try {
            JSONObject messagePart = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String currentDateTime = sdf.format(new Date());

            messagePart.put("text",
                    "Current datetime is " + currentDateTime + ". " +
                            "You are an assistant that extracts structured reminder data. " +
                            "From the following user input, extract only the following fields in **strict JSON format**: " +
                            "`title` (1-10 words), `description` (1-20 words), `scheduledTime` (DD/MM/YYYY HH:MM). " +
                            "Do NOT include any extra explanation or text. Only output a pure JSON object. " +
                            "If any fields are missing or invalid, return a JSON object like: {\"error\": \"Missing title/description/scheduledTime\"}.\n\n" +
                            "User input: " + userPrompt);

            JSONObject content = new JSONObject();
            content.put("parts", new JSONArray().put(messagePart));

            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", new JSONArray().put(content));

            Request request = new Request.Builder()
                    .url(GEMINI_API_URL + "?key=" + API_KEY)
                    .post(RequestBody.create(requestBody.toString(), JSON))
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        responseTextView.setText("Network error. Please try again.");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            processGeminiResponse(responseBody);
                        } else {
                            responseTextView.setText("Something went wrong. Please try again.");
                        }
                    });
                }
            });

        } catch (JSONException e) {
            progressBar.setVisibility(View.GONE);
            responseTextView.setText("Something went wrong. Please try again.");
        }
    }

    private void processGeminiResponse(String responseJson) {
        try {
            JSONObject fullResponse = new JSONObject(responseJson);
            JSONArray candidates = fullResponse.optJSONArray("candidates");
            if (candidates == null || candidates.length() == 0) {
                responseTextView.setText("I couldn't understand that. Try again with a clearer prompt.");
                return;
            }

            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.optJSONArray("parts");
            if (parts == null || parts.length() == 0) {
                responseTextView.setText("I couldn't understand that. Try again with a clearer prompt.");
                return;
            }

            String textResponse = parts.getJSONObject(0).getString("text").trim();

            // Attempt to parse text as JSON
            JSONObject jsonResponse;
            try {
                jsonResponse = new JSONObject(textResponse);
            } catch (JSONException e) {
                responseTextView.setText(textResponse);
                return;
            }

            if (jsonResponse.has("title") && jsonResponse.has("description") && jsonResponse.has("scheduledTime")) {
                String title = jsonResponse.getString("title");
                String description = jsonResponse.getString("description");
                String scheduledTimeStr = jsonResponse.getString("scheduledTime");

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date scheduledTime = format.parse(scheduledTimeStr);

                Reminder reminder = new Reminder();
                reminder.setTitle(title);
                reminder.setContent(description);
                reminder.setTime(scheduledTimeStr);

                responseTextView.setText("Reminder created: " + title + " scheduled for " + scheduledTimeStr);
            } else if (jsonResponse.has("message")) {
                responseTextView.setText(jsonResponse.getString("message"));
            } else {
                responseTextView.setText("I couldn't extract reminder details. Try again.");
            }
        } catch (JSONException | ParseException e) {
            responseTextView.setText("I couldn't understand that. Try again with a clearer prompt.");
        }
    }
}
