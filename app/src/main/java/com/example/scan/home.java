package com.example.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView employeeManagementBox = findViewById(R.id.employeeManagementBox);
        TextView billsBox = findViewById(R.id.billsBox);
        TextView historyBox = findViewById(R.id.historyBox);
        TextView offersBox = findViewById(R.id.offersBox);
        ImageButton profileButton = findViewById(R.id.profile);
        ImageButton Logout = findViewById(R.id.Logout);


        // Set click listener for the profile button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(home.this, ProfileActivity2.class);
                startActivity(intent);
            }
        });

        // Set click listener for the profile button
        offersBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(home.this, viewoffer.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the employeeManagementBox TextView
        employeeManagementBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the insertemp activity
                Intent intent = new Intent(home.this, viewemp.class);
                startActivity(intent);
            }
        });


        billsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the insertemp activity
                Intent intent = new Intent(home.this, adminpaymentdetails.class);
                startActivity(intent);
            }
        });

        historyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the insertemp activity
                Intent intent = new Intent(home.this, historydetails.class);
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the insertemp activity
                Intent intent = new Intent(home.this, usertype.class);
                startActivity(intent);
            }
        });

    }
}
