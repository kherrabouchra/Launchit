package com.example.launchit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RessourcesActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ressources);
        Intent intent = getIntent();
        long userId = intent.getLongExtra("USER_ID", -1);

        Button goBack = findViewById(R.id.GoBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(RessourcesActivity.this, DashboardActivity.class);
                 intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }


        });


    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView sendM = findViewById(R.id.sendMess);
        sendM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(RessourcesActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }


        });


    }
}
