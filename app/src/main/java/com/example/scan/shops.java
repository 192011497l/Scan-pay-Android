package com.example.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class shops extends AppCompatActivity {

    LinearLayout containerLayout;
    private PopupMenu popupMenu;
    private boolean isPopupMenuShowing = false;
    BottomNavigationView bottomNavigationView;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops);

        containerLayout = findViewById(R.id.containerLayout);
        showAdminsData();
    }

    private void showAdminsData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    final String shopName = adminSnapshot.child("shopname").getValue(String.class);
                    String shopAddress = adminSnapshot.child("address").getValue(String.class);
                    // String imageUrl = adminSnapshot.child("imageURL").getValue(String.class);

                    // Inflate the CardView layout
                    View cardViewLayout = getLayoutInflater().inflate(R.layout.shops_layout, null);

                    // Find Views inside the CardView layout
                    //ImageView shopImageView = cardViewLayout.findViewById(R.id.shopImageView);
                    TextView shopNameTextView = cardViewLayout.findViewById(R.id.shopNameTextView);
                    TextView shopAddressTextView = cardViewLayout.findViewById(R.id.shopAddressTextView);

                    // Load the image using Picasso
                    //Picasso.get().load(imageUrl).into(shopImageView);

                    // Set shopName and shopAddress to TextViews
                    shopNameTextView.setText("Shop Name: " + shopName);
                    shopAddressTextView.setText("Shop Address: " + shopAddress);

                    // Set onClickListener for the CardView
                    cardViewLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Pass the selected shopName to Covers activity

                            UserData userData = UserData.getInstance();
                            userData.setshopName(shopName);

                            Intent intent = new Intent(shops.this, custmerhome.class);


                            startActivity(intent);
                        }
                    });

                    // Add the inflated CardView to the container layout
                    containerLayout.addView(cardViewLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
        ImageButton profileButton = findViewById(R.id.profile);
        ImageButton Logout = findViewById(R.id.Logout);
//        ImageButton cartButton = findViewById(R.id.cart);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(shops.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the insertemp activity
                Intent intent = new Intent(shops.this, usertype.class);
                startActivity(intent);
            }
        });
//        cartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event, e.g., start the ProfileActivity
//                Intent intent = new Intent(shops.this, viewcart.class);
//                startActivity(intent);
//            }
//        });
    }

}