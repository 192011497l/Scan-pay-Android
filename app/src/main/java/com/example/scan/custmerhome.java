package com.example.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class custmerhome extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custmerhome);



        // Find the TextView by its ID
        TextView scanTextView = findViewById(R.id.btn_scanner);

        // Set OnClickListener to start custmerhome activity
        scanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(custmerhome.this, QRScanner.class);
                startActivity(intent);
            }
        });

        TextView paymentsTextView = findViewById(R.id.payments);
        TextView historyBox = findViewById(R.id.historyBox);
        ImageButton profileButton = findViewById(R.id.profile);
        TextView offersBox = findViewById(R.id.offersBox);
        ImageButton Logout = findViewById(R.id.Logout);
        ImageButton cartButton = findViewById(R.id.cart);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(custmerhome.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        offersBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(custmerhome.this, viewoffer2.class);
                startActivity(intent);
            }
        });



        // Set OnClickListener to start PaymentsActivity
        paymentsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(custmerhome.this, paymentdetails.class);
                startActivity(intent);
            }
        });


        // Set click listener for the profile button
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(custmerhome.this, viewcart.class);
                startActivity(intent);
            }
        });

        historyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the insertemp activity
                Intent intent = new Intent(custmerhome.this, historydetails.class);
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the insertemp activity
                Intent intent = new Intent(custmerhome.this, usertype.class);
                startActivity(intent);
            }
        });
    }
}
