package com.example.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class adminpaymentdetails extends AppCompatActivity  {

    LinearLayout coversContainer;
    String shopName;
    TextView add;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpaymentdetails);

        coversContainer = findViewById(R.id.coversContainer);



        // Set its visibility to GONE

        // Show all covers for the specific shop
        showCoversForShop();
        ImageButton profileButton = findViewById(R.id.profile);
        ImageButton homeIcon = findViewById(R.id.homeIcon);
//        ImageButton cart = findViewById(R.id.cart);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(adminpaymentdetails.this, ProfileActivity2.class);
                startActivity(intent);
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(adminpaymentdetails.this, home.class);
                startActivity(intent);
            }
        });

    }


    private void showCoversForShop() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("payments");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // No data found for the specific shop
                    Log.e("Covers", "No data found");
                    TextView noOffersTextView = new TextView(adminpaymentdetails.this);
                    noOffersTextView.setText("                        No Payments available");
                    coversContainer.addView(noOffersTextView);

                    return;
                }

                for (DataSnapshot coversSnapshot : snapshot.getChildren()) {
                    String paymentId = coversSnapshot.child("paymentId").getValue(String.class);
                    String paymentDate = coversSnapshot.child("paymentDate").getValue(String.class);
                    String paymentStatus = coversSnapshot.child("paymentStatus").getValue(String.class);

                    // Only display payments that are not completed
                    if (!"completed".equals(paymentStatus)) {
                        // Inflate the item_cover.xml layout
                        View coverItemView = getLayoutInflater().inflate(R.layout.activity_adminitempayments, coversContainer, false);

                        TextView namee = coverItemView.findViewById(R.id.paymentIdTextView);
                        TextView rolee = coverItemView.findViewById(R.id.paymentDateTextView);
                        TextView mobile = coverItemView.findViewById(R.id.paymentStatusTextView);
                        namee.setText("paymentId: " + paymentId);
                        mobile.setText("paymentDate: " + paymentDate);
                        rolee.setText("paymentStatus: " + paymentStatus);
                        coverItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(adminpaymentdetails.this, adminviewpayment.class);
                                intent.putExtra("paymentId", paymentId);
                                intent.putExtra("paymentDate", paymentDate);
                                intent.putExtra("paymentStatus", paymentStatus);
                                startActivity(intent);
                            }
                        });

                        // Add the inflated layout to the coversContainer
                        coversContainer.addView(coverItemView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Covers", "Database error: " + error.getMessage());
            }
        });
    }
}