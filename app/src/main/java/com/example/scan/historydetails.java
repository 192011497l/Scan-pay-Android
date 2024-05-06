package com.example.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class historydetails extends AppCompatActivity  {

    LinearLayout coversContainer;
    String shopName;
    TextView add;
    String username;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historydetailsactivity);

        coversContainer = findViewById(R.id.coversContainer);



        // Set its visibility to GONE

        // Show all covers for the specific shop
        showCoversForShop();

    }


    private void showCoversForShop() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("payment_history");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // No data found for the specific shop
                    Log.e("Covers", "No data found");
                    TextView noOffersTextView = new TextView(historydetails.this);
                    noOffersTextView.setText("         No History available");
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
                        View coverItemView = getLayoutInflater().inflate(R.layout.activity_itemhistory, coversContainer, false);

                        TextView namee = coverItemView.findViewById(R.id.paymentIdTextView);
                        TextView rolee = coverItemView.findViewById(R.id.paymentDateTextView);
                        TextView mobile = coverItemView.findViewById(R.id.paymentStatusTextView);
                        namee.setText("paymentId: " + paymentId);
                        mobile.setText("paymentDate: " + paymentDate);
                        rolee.setText("paymentStatus: " + paymentStatus);
                        coverItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(historydetails.this, viewhistory.class);
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