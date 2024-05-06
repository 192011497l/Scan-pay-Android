package com.example.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class viewoffer extends AppCompatActivity {

    LinearLayout coversContainer;
    String shopName;
    Button add;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewoffer);

        coversContainer = findViewById(R.id.coversContainer);
        add = findViewById(R.id.add);

        // Get the shopName from the UserData singleton class
        UserData userData = UserData.getInstance();
        shopName = userData.getshopName();

        // Show all covers for the specific shop
        showCoversForShop();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewoffer.this, insertoffer.class);
                startActivity(intent);
            }
        });
        ImageButton profileButton = findViewById(R.id.profile);
        ImageButton homeIcon = findViewById(R.id.homeIcon);
//        ImageButton cart = findViewById(R.id.cart);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(viewoffer.this, ProfileActivity2.class);
                startActivity(intent);
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(viewoffer.this, home.class);
                startActivity(intent);
            }
        });

//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event, e.g., start the ProfileActivity
//                Intent intent = new Intent(viewoffer.this, viewcart.class);
//                startActivity(intent);
//            }
//        });
    }
    private void showCoversForShop() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("offers");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // No data found for the specific shop
                    Log.e("Covers", "No data found for covers in shop: " + shopName);
                    TextView noOffersTextView = new TextView(viewoffer.this);
                    noOffersTextView.setText("No offers available");
                    coversContainer.addView(noOffersTextView);

                    return;

                }

                for (DataSnapshot coversSnapshot : snapshot.getChildren()) {
                    Log.d("Covers", "Processing entry for covers");

                    String offerId = coversSnapshot.getKey(); // Get the offer ID
                    String offer = coversSnapshot.child("offer").getValue(String.class);
                    String expiryDate = coversSnapshot.child("expiryDate").getValue(String.class);
                    String offerDetails = coversSnapshot.child("details").getValue(String.class);

                    // Inflate the item_offer.xml layout
                    View coverItemView = getLayoutInflater().inflate(R.layout.activity_itemoffer, coversContainer, false);

                    TextView offerTextView = coverItemView.findViewById(R.id.offer);
                    TextView expireTextView = coverItemView.findViewById(R.id.expire);
                    TextView detailsTextView = coverItemView.findViewById(R.id.details);
                    Button removeButton = coverItemView.findViewById(R.id.priceTextView4);

                    offerTextView.setText("Offer: " + offer);
                    expireTextView.setText("Expiry Date: " + expiryDate);
                    detailsTextView.setText("Details: " + offerDetails);

                    // Set click listener for the remove button
                    removeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Remove the offer from the database
                            DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers").child(offerId);
                            offerRef.removeValue();
                            // Remove the view from the layout
                            coversContainer.removeView(coverItemView);
                        }
                    });

                    // Add the inflated layout to the coversContainer
                    coversContainer.addView(coverItemView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Covers", "Database error: " + error.getMessage());
            }
        });
    }
}
