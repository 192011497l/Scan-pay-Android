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

public class viewhistory extends AppCompatActivity {

    LinearLayout coversContainer;
    String PaymentId;
    TextView amountview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewhistory);
        amountview =findViewById(R.id.amountTextView);

        coversContainer = findViewById(R.id.coversContainer);
        Intent intent = getIntent();

        PaymentId = intent.getStringExtra("paymentId");
        Log.d("Covers", "Clicked Payment ID: " + PaymentId);
        // Show all covers for the specific shop
        showCoversForShop();
    }

    private void showCoversForShop() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("payment_history").child(PaymentId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("Covers", "No payments found in database");
                    TextView noOffersTextView = new TextView(viewhistory.this);
                    noOffersTextView.setText("                No History available");
                    coversContainer.addView(noOffersTextView);

                    return;

                }

                String paymentId = snapshot.child("paymentId").getValue(String.class);
                Log.d("Covers", "Payment ID from database: " + paymentId);

                Long amount = snapshot.child("amount").getValue(Long.class);
                String paymentStatus = snapshot.child("paymentStatus").getValue(String.class);

                Log.d("Covers", "Amount: " + amount);
                amountview.setText("Amount:   " + amount);

                for (DataSnapshot itemSnapshot : snapshot.child("items").getChildren()) {
                    String itemName = itemSnapshot.child("name").getValue(String.class);
                    String itemPrice = itemSnapshot.child("price").getValue(String.class);
                    String itemQuantity = itemSnapshot.child("quantity").getValue(String.class);
                    Integer itemTotalPrice = itemSnapshot.child("total").getValue(Integer.class);

                    // Now, you can display or process each item's details as needed
                    Log.d("Covers", "Item Name: " + itemName);
                    Log.d("Covers", "Item Price: " + itemPrice);
                    Log.d("Covers", "Item Quantity: " + itemQuantity);
                    Log.d("Covers", "Item Total Price: " + itemTotalPrice);

                    // Inflate the item layout
                    View itemLayout = getLayoutInflater().inflate(R.layout.activity_itempaymentsdetails, coversContainer, false);

                    TextView itemNameTextView = itemLayout.findViewById(R.id.itemNameTextView);
                    TextView itemPriceTextView = itemLayout.findViewById(R.id.itemPriceTextView);
                    TextView itemQuantityTextView = itemLayout.findViewById(R.id.itemQuantityTextView);
                    TextView totalpriceTextView = itemLayout.findViewById(R.id.totalPriceTextView);

                    itemNameTextView.setText("" + itemName);
                    itemPriceTextView.setText("" + itemPrice);
                    itemQuantityTextView.setText("" + itemQuantity);
                    totalpriceTextView.setText(String.valueOf(itemTotalPrice));

                    coversContainer.addView(itemLayout);
                    Log.d("Covers", "Item details added to layout");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Covers", "Database error: " + error.getMessage());
            }
        });
    }
}
