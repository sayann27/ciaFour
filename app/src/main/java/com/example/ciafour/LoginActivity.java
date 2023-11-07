package com.example.ciafour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private CheckBox checkboxRememberMe;
    private Button btnLogin, btnRegister;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
//        db = openOrCreateDatabase("GroceryDB", Context.MODE_PRIVATE, null);


        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            // Check if the user exists in the database
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
            System.out.println(cursor);
            if (cursor.moveToFirst()) {
                // User exists, allow login
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, ExpenseListActivity.class);
//                    startActivity(intent);

                // If "Remember Me" is checked, save the credentials in shared preferences
                if (checkboxRememberMe.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.apply();
                }

                // Optionally, navigate to another activity (e.g., the main application screen) after successful login.
            } else {
                Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        });

        // Handle the "Register" button click to navigate to the registration activity
        btnRegister.setOnClickListener(view -> {
            Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(registrationIntent);
        });

        // Autofill the username and password if "Remember Me" is checked in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String rememberedUsername = sharedPreferences.getString("username", "");
        String rememberedPassword = sharedPreferences.getString("password", "");

        if (!rememberedUsername.isEmpty() && !rememberedPassword.isEmpty()) {
            etUsername.setText(rememberedUsername);
            etPassword.setText(rememberedPassword);
            checkboxRememberMe.setChecked(true);
        }
    }
}
