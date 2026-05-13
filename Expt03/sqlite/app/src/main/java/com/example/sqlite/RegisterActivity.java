package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText edtRegisterUsername, edtRegisterEmail, edtRegisterPassword, edtRegisterConfirmPassword;
    Button btnRegister;
    TextView txtGoToLogin;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtRegisterUsername = findViewById(R.id.edtRegisterUsername);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        edtRegisterConfirmPassword = findViewById(R.id.edtRegisterConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtGoToLogin = findViewById(R.id.txtGoToLogin);

        databaseHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(view -> {

            String username = edtRegisterUsername.getText().toString().trim();
            String email = edtRegisterEmail.getText().toString().trim();
            String password = edtRegisterPassword.getText().toString().trim();
            String confirmPassword = edtRegisterConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                edtRegisterUsername.setError("Username is required");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                edtRegisterEmail.setError("Email is required");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtRegisterEmail.setError("Enter a valid email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                edtRegisterPassword.setError("Password is required");
                return;
            }

            if (password.length() < 6) {
                edtRegisterPassword.setError("Password must be at least 6 characters");
                return;
            }

            if (!password.equals(confirmPassword)) {
                edtRegisterConfirmPassword.setError("Passwords do not match");
                return;
            }

            boolean emailExists = databaseHelper.checkEmailExists(email);

            if (emailExists) {
                Toast.makeText(RegisterActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean registered = databaseHelper.registerUser(username, email, password);

            if (registered) {
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });

        txtGoToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}