package com.example.launchit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHandler(this);

        TextView createAccountTextView = findViewById(R.id.your_highway2);
        setupCreateAccountLink(createAccountTextView);

        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.pw);
        Button loginButton = findViewById(R.id.loginbtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isValidInput(email, password)) {
                    loginUser(email, password);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupCreateAccountLink(TextView textView) {
        SpannableString spannableString = new SpannableString(textView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                navigateToSignUp();
            }
        };

        spannableString.setSpan(clickableSpan, 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private boolean isValidInput(String email, String password) {
        // Implement your input validation logic here
        return !email.isEmpty() && !password.isEmpty();
    }

    private void loginUser(String email, String password) {
        User user = dbHelper.getUser(email, password);

        if (user != null) {
            // Log user information before starting DashboardActivity
            Log.d("LoginUser", "User ID: " + user.getId());
            Log.d("LoginUser", "User Email: " + user.getEmail());

            // Pass user information to DashboardActivity
            Intent dashboardIntent = new Intent(MainActivity.this, DashboardActivity.class);
            dashboardIntent.putExtra("USER_ID", user.getId());
            dashboardIntent.putExtra("USER_EMAIL", user.getEmail());
            startActivity(dashboardIntent);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
    private void navigateToSignUp() {
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboard() {
        Intent dashboardIntent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(dashboardIntent);
        finish();
    }
}
