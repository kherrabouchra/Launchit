
package com.example.launchit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class DashboardActivity extends AppCompatActivity {
    long userId;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Retrieve user information from the intent extras
        Intent intent = getIntent();
        userId = intent.getLongExtra("USER_ID", -1);
        db = new DatabaseHandler(this);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button addReqBtn = findViewById(R.id.addReqBtn);
        TextView fullNameTextView = findViewById(R.id.fullname);
        ImageButton aboutBtn = findViewById(R.id.aboutBtn);
        ImageButton ressBtn = findViewById(R.id.RessBtn);
        ImageButton supportBtn = findViewById(R.id.SuppportBtn);
        // Use the retrieved user information
        String fullName = getUserFullName(userId);
        fullNameTextView.setText(fullName);
        ImageButton message = findViewById(R.id.messageBtn);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(DashboardActivity.this, MessageActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
        Button notif = findViewById(R.id.notif);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
        displayRegisterRequests(userId);

        Log.d("iddd", String.valueOf(userId));


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        addReqBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DashboardActivity.this, FormActivity.class);
                Intent originalIntent = getIntent();
                long userId = originalIntent.getLongExtra("USER_ID", -1);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // no_animation is an empty animation resource
            }
        });


        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start InterFormActivity with slide-up animation
                Intent intent = new Intent(DashboardActivity.this, About.class);

                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start InterFormActivity with slide-up animation
                Intent intent = new Intent(DashboardActivity.this, SupportActivity.class);

                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
        ressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start InterFormActivity with slide-up animation
                Intent intent = new Intent(DashboardActivity.this, RessourcesActivity.class);

                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
    }

    private String getUserFullName(long userId) {
        // Implement your logic to retrieve user full name based on userId
        // For example, you might have a method in your DatabaseHandler to get user information
        DatabaseHandler db = new DatabaseHandler(this);
        User user = db.getUserById(userId);

        if (user != null) {
            return (user.getFirstName() + " " + user.getLastName());
        } else {
            return "Unknown User";
        }
    }


    private void displayRegisterRequests(long id) {
        DatabaseHandler db = new DatabaseHandler(this);
        List<RegisterRequest> registerRequests = db.getRequestsByUser(id);

        if (registerRequests.isEmpty()) {
            findViewById(R.id.requests).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.requests).setVisibility(View.INVISIBLE);

            TextView textView11 = findViewById(R.id.textView11);
            textView11.setText("Requests (" + registerRequests.size() + ")");

            // Assuming you have a LinearLayout in your dashboard XML with id "cardLayout"
            LinearLayout cardLayout = findViewById(R.id.cardLayout);
            cardLayout.removeAllViews(); // Clear existing views if any

            // Iterate over the registerRequests list and create a card for each item
            for (RegisterRequest request : registerRequests) {
                View cardView = createCardView(request);
                cardLayout.addView(cardView);
            }
        }
    }

    private View createCardView(RegisterRequest request) {
        // Inflate the card layout from the separate XML file (card.xml)
        View cardView = LayoutInflater.from(this).inflate(R.layout.card, null);

        // Now, find views within the inflated card layout
        TextView reqIDTextView = cardView.findViewById(R.id.reqID);
        TextView stateTextView = cardView.findViewById(R.id.state3);

        reqIDTextView.setText(String.valueOf(request.getReqID()));

        TextView submissionDateTextView = cardView.findViewById(R.id.textView26);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Check if submission date is not null before formatting
        if (request.getSubmissionDate() != null) {
            submissionDateTextView.setText("Submission date: " + sdf.format(request.getSubmissionDate()));

            // Calculate expected result date by adding 1 day to the submission date
            TextView expectedResultDateTextView = cardView.findViewById(R.id.textView27);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(request.getSubmissionDate());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date expectedResultDate = calendar.getTime();
            expectedResultDateTextView.setText("Expected result date: " + sdf.format(expectedResultDate));
        } else {
            // Handle the case when submission date is null
            submissionDateTextView.setText("Submission date: N/A");
        }

        if ("accepted".equals(request.getState())) {
            stateTextView.setText("Accepted");
            stateTextView.setTextColor(ContextCompat.getColor(this, R.color.green));
        } else if ("refused".equals(request.getState())) {
            stateTextView.setText("Refused");
            stateTextView.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
        }


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the state to "accepted" when the card is clicked
               db.updateRequestStateToRefused(request.getReqID());


                Intent intent = new Intent(DashboardActivity.this, ReqDetailsActivity.class);
                Intent originalIntent = getIntent();
                long userId = originalIntent.getLongExtra("USER_ID", -1);

                intent.putExtra("USER_ID", userId);
                intent.putExtra("REQ_ID", request.getReqID());

                startActivity(intent);
            }
        });


        return cardView;
    }

    private void insertNotificationCard(String message, String time) {
        // Send broadcast intent with message and time
        Intent broadcastIntent = new Intent("com.example.launchit.NEW_NOTIFICATION");
        broadcastIntent.putExtra("MESSAGE", message);
        broadcastIntent.putExtra("TIME", time);
        sendBroadcast(broadcastIntent);

    }





}