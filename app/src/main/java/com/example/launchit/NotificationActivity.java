package com.example.launchit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        Intent intent = getIntent();
        long userId = intent.getLongExtra("USER_ID", -1);

        Button goBack = findViewById(R.id.GoBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(NotificationActivity.this, DashboardActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Display notification cards
        DisplayNotificationCards(userId);
    }
    public static String getTimeAgo(Date pastDate) {
        Date currentDate = new Date();
Log.d("times", currentDate.toString());
Log.d("times", pastDate.toString());

        long timeDifferenceMillis = currentDate.getTime() - pastDate.getTime();

        long seconds = Math.abs(timeDifferenceMillis / 1000);
        long minutes = Math.abs(seconds / 60);
        long hours = Math.abs(minutes / 60);
        long days = Math.abs(hours / 24);

        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else {
            return seconds + (seconds == 1 ? " second ago" : " seconds ago");
        }
    }


    private void DisplayNotificationCards(long userId) {
        // Retrieve notifications from the database
        DatabaseHandler db = new DatabaseHandler(this);
        List<Notification> notifications = db.getNotificationsByUser(userId);

        // Get the notifications layout
        LinearLayout notificationsLayout = findViewById(R.id.notificationsLayout);

        // Iterate over the notifications list and create a card for each item
        for (Notification notification : notifications) {
            // Inflate the notification card layout
            View notificationCard = LayoutInflater.from(this).inflate(R.layout.notif_card, null);

            // Set the message and time in the card
            TextView messageText = notificationCard.findViewById(R.id.messageText);
            TextView notifTime = notificationCard.findViewById(R.id.notifTime);
            TextView nonotif = findViewById(R.id.noNotif);
            messageText.setText(notification.getMessage());

                notifTime.setText(getTimeAgo(notification.getTime()));
                nonotif.setVisibility(View.GONE);
                if (notificationsLayout != null) {
                    notificationsLayout.addView(notificationCard, 0); // Add at the beginning

            }

        // If there were no notifications, show a message
        if (notifications.isEmpty()) {
            TextView noNotif = findViewById(R.id.noNotif);
            noNotif.setVisibility(View.VISIBLE);
        }



        }

}}
