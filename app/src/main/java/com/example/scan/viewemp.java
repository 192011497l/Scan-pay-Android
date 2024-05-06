package com.example.scan;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class viewemp extends AppCompatActivity  {

    LinearLayout coversContainer;
    String shopName;
    Button add;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewemp);

        coversContainer = findViewById(R.id.coversContainer);
        add=findViewById(R.id.add);
        // Get the shopName from the UserData singleton class
        UserData userData = UserData.getInstance();
        shopName = userData.getshopName();
        // Show all covers for the specific shop
        showCoversForShop();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewemp.this, insertemp.class);
                startActivity(intent);
            }
        });
        ImageButton profileButton = findViewById(R.id.profile);
        ImageButton homeIcon = findViewById(R.id.homeIcon);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(viewemp.this, ProfileActivity2.class);
                startActivity(intent);
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start the ProfileActivity
                Intent intent = new Intent(viewemp.this, home.class);
                startActivity(intent);
            }
        });

//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event, e.g., start the ProfileActivity
//                Intent intent = new Intent(viewemp.this, viewcart.class);
//                startActivity(intent);
//            }
//        });
    }


    private void showCoversForShop() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("insertemp");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (!snapshot.exists()) {
                    // No data found for the specific shop
                    Log.e("Covers", "No data found : " + shopName);
                    TextView noOffersTextView = new TextView(viewemp.this);
                    noOffersTextView.setText("             No Employees available");
                    coversContainer.addView(noOffersTextView);

                    return;

                }


                for (DataSnapshot coversSnapshot : snapshot.getChildren()) {
                    Log.d("Covers", "Processing entry for covers");

                    String address = coversSnapshot.child("addressd").getValue(String.class);
                    String email = coversSnapshot.child("email").getValue(String.class);
                    String mobileNumber = coversSnapshot.child("mobileNumber").getValue(String.class);
                    String age = coversSnapshot.child("age").getValue(String.class);
                    String qualification = coversSnapshot.child("qualification").getValue(String.class);
                    String experience = coversSnapshot.child("experience").getValue(String.class);
                    String name = coversSnapshot.child("name").getValue(String.class);
                    String role = coversSnapshot.child("role").getValue(String.class);
                    String status = coversSnapshot.child("status").getValue(String.class);




                        // Inflate the item_cover.xml layout
                        View coverItemView = getLayoutInflater().inflate(R.layout.activity_itememp, coversContainer, false);


                        TextView namee = coverItemView.findViewById(R.id.priceTextView);
                        TextView rolee = coverItemView.findViewById(R.id.priceTextView1);
                        TextView mobile = coverItemView.findViewById(R.id.priceTextView2);
                        TextView addres = coverItemView.findViewById(R.id.priceTextView3);
                    Button editButton = coverItemView.findViewById(R.id.priceTextView4);

                    if ("Activate".equals(status)) {
                        editButton.setText("Inactivate");

                    } else if ("Inactivate".equals(status)) {
                        editButton.setText("Activate");

                    }

                    namee.setText("Name: " + name);
                    addres.setText("Address: " + address);
                    mobile.setText("Price: " + mobileNumber);
                    rolee.setText("Price: " + role);

                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Check the current status


                            // Change the status based on the current status
                            if ("Activate".equals(status)) {

                                changeEmployeeStatus(name, "Activate", "Inactivate");
                            } else if ("Inactivate".equals(status)) {

                                changeEmployeeStatus(name, "Inactivate", "Activate");
                            }
                        }
                    });

                        // Set a click listener for the Customize button
                        coverItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Handle Customize button click
                                // Start the Customize activity and pass image URL and price as extras
                                showPopupDetails(address, mobileNumber, email,age, qualification, experience, name,role);
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


    private void showPopupDetails(String address, String mobileNumber, String email, String age, String qualification, String experience, String name, String role) {
        // Inflate the index_popviewxml.xml layout
        View popupView = getLayoutInflater().inflate(R.layout.popup_details, null);


        TextView namee = popupView.findViewById(R.id.name);
        TextView emaill = popupView.findViewById(R.id.email);
        TextView numberr = popupView.findViewById(R.id.number);
        TextView agee = popupView.findViewById(R.id.age);
        TextView rolee = popupView.findViewById(R.id.role);
        TextView experiencee = popupView.findViewById(R.id.experience);
        TextView qualificationn = popupView.findViewById(R.id.qualification);
        TextView addresss = popupView.findViewById(R.id.address);


        Button closeButton = popupView.findViewById(R.id.closeButton);


        namee.setText("Name:         " + name);
        emaill.setText("Email         " + email);
        numberr.setText("Mobile Number: " + mobileNumber);
        agee.setText("Age             : " + age);
        rolee.setText("Role: " + role);
        experiencee.setText("Experience:       " + experience);
        qualificationn.setText("Qualification:   " + qualification);
        addresss.setText("Address: " + address);



        // Create and show the popup
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        // Set a dismiss listener to close the popup when the background is clicked
        popupWindow.setOnDismissListener(() -> {
            // Update any UI elements or perform actions when the popup is dismissed
        });

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Set additional properties for the popup window, if needed
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);


        // Now, you have a PopupWindow with your custom layout displayed on top of the activity
    }
    private void changeEmployeeStatus(String employeeName, String oldStatus, String newStatus) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("insertemp");

        reference.orderByChild("name").equalTo(employeeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot employeeSnapshot : snapshot.getChildren()) {
                        String status = employeeSnapshot.child("status").getValue(String.class);

                        if (status != null && status.equals(oldStatus)) {
                            // Update the status to the new status
                            employeeSnapshot.getRef().child("status").setValue(newStatus);
                            refresh();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChangeStatus", "Database error: " + error.getMessage());
            }
        });
    }
    private void refresh(){
        coversContainer.removeAllViews(); // Clear the existing views
        showCoversForShop();
    }



}