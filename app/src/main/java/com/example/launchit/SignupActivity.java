package com.example.launchit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText,
            passwordEditText, confirmPasswordEditText, phoneEditText;

    private DatabaseHandler dbHelper;  // Declare dbHelper at the class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.editTextText);
        lastNameEditText = findViewById(R.id.editTextText3);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);
        phoneEditText = findViewById(R.id.editTextPhone2);

        Button signupButton = findViewById(R.id.nextBtn2);

        // Initialize dbHelper here
        dbHelper = new DatabaseHandler(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform field validation
                if (validateFields()) {
                    // Fields are valid, proceed with signup logic
                    User newUser = new User();

                    dbHelper.addUser(
                        firstNameEditText.getText().toString().trim(),
                  lastNameEditText.getText().toString().trim(),
                    emailEditText.getText().toString().trim(),
                  passwordEditText.getText().toString(),
                  phoneEditText.getText().toString().trim());

                    showToast("Signup successful!");

                    // Navigate back to the login activity (MainActivity)
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finish the SignupActivity so the user can't navigate back to it
                }
            }
        });
    }

    private boolean validateFields() {
        // Get values from input fields
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String phone = phoneEditText.getText().toString().trim();

        // Check if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            showToast("Please fill in all fields");
            return false;
        }

        // Check if the email has a valid format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email format");
            return false;
        }

        // Add more validation checks as needed

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }

        // Add additional validation checks as needed

        // All fields are valid
        return true;
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
