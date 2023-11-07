package com.example.ciafour;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etPhoneNumber;
    private Button btnRegister;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnRegister = findViewById(R.id.btnRegister);

        // Create or open the SQLite database
        db = openOrCreateDatabase("UserDataDB", Context.MODE_PRIVATE, null);

        // Create the user data table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, phone_number TEXT)");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();

                if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert user registration data into the database
                    ContentValues values = new ContentValues();
                    values.put("username", username);
                    values.put("password", password);
                    values.put("phone_number", phoneNumber);
                    db.insert("users", null, values);

                    Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    sendSMS(phoneNumber, "YOU HAVE BEEN REGISTERED WITH OUR APP");

                    // Optionally, you can navigate to another activity (e.g., login) after successful registration.
                }
            }
        });
    }
    private void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE);

        try {
            smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent);
            Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "SMS sending failed: " + e.getMessage(), Toast.LENGTH_LONG).show();


        }

    }
}
