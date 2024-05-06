package com.example.scan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class viewcart extends AppCompatActivity implements PaymentResultListener {

    LinearLayout coversContainer;
    DatabaseReference cartRef;
    double totalPrice = 0.0;
    double amount = 0.0; // Total amount payable
    TextView amountTextView;
    Button payButton;
    DataSnapshot dataSnapshot;
    TextView cartEmptyTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcart);
        coversContainer = findViewById(R.id.coversContainer);
        amountTextView = findViewById(R.id.amountTextView);
        payButton = findViewById(R.id.payButton);
        cartEmptyTextView = findViewById(R.id.cartEmptyTextView);

        cartRef = FirebaseDatabase.getInstance().getReference().child("cart");

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSnapshot = snapshot;
                if (dataSnapshot.exists()) {
                    cartEmptyTextView.setVisibility(View.GONE);
                    for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                        final String cartItemString = cartSnapshot.getKey(); // Using the cart item key as identifier

                        View cartItemView = LayoutInflater.from(viewcart.this).inflate(R.layout.activity_itemcart, coversContainer, false);

                        TextView nameTextView = cartItemView.findViewById(R.id.nameTextView);
                        TextView quantityTextView = cartItemView.findViewById(R.id.quantityTextView);
                        TextView priceTextView = cartItemView.findViewById(R.id.priceTextView);
                        TextView totalPriceTextView = cartItemView.findViewById(R.id.totalpriceTextView);
                        Button removeButton = cartItemView.findViewById(R.id.removeButton);

                        String[] parts = cartSnapshot.getValue(String.class).split("\\s+");
                        final String name = parts[1];
                        final String quantity = parts[3];
                        final String price = parts[5];

                        nameTextView.setText("Name: " + name);
                        quantityTextView.setText("Quantity: " + quantity);
                        priceTextView.setText("Price: " + price);

                        double itemTotalPrice = Double.parseDouble(price) * Integer.parseInt(quantity);
                        totalPrice += itemTotalPrice;
                        totalPriceTextView.setText("Total Price: " + itemTotalPrice);

                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("cart").child(cartItemString);
                                offerRef.removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Item removed from the database, now remove from UI
                                                coversContainer.removeView(cartItemView);
                                                double itemTotalPrice = Double.parseDouble(price) * Integer.parseInt(quantity);
                                                totalPrice -= itemTotalPrice;
                                                amount = totalPrice; // Update the total amount
                                                applyDiscounts(); // Apply discounts
                                                amountTextView.setText("Amount Payable: " + amount);

                                                // Remove the item from the dataSnapshot
                                                cartSnapshot.getRef().removeValue();
                                                recreate();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Firebase", "Failed to remove item from database: " + e.getMessage());
                                                // Handle failure to remove item from database
                                            }
                                        });
                            }
                        });

                        coversContainer.addView(cartItemView);
                    }

                    amount = totalPrice; // Set the initial amount to the total price
                    applyDiscounts(); // Apply discounts
                    amountTextView.setText("Amount Payable: " + amount);
                } else {
                    cartEmptyTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read value.", databaseError.toException());
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSnapshot != null) {
                    startRazorpayPayment();
                } else {
                    Log.e("Firebase", "DataSnapshot is null.");
                }
            }
        });
    }

    private void applyDiscounts() {
        // Check for discount eligibility
        if (totalPrice > 5000) {
            // Apply 5% discount
            double discount = totalPrice * 0.05;
            amount -= discount;
        }
        if (totalPrice > 10000) {
            // Apply additional 10% discount
            double discount = totalPrice * 0.10;
            amount -= discount;
        }
        // Ensure amount doesn't go below zero
        if (amount < 0) {
            amount = 0;
        }
    }

    private void startRazorpayPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_5rwYE521v89m4u"); // Replace with your Razorpay key

        try {
            JSONObject options = new JSONObject();
            options.put("name", "CodingSTUFF");
            options.put("description", "Payment for items");
            options.put("currency", "INR");
            options.put("amount", (int) (amount * 100)); // Razorpay accepts amount in paise
            options.put("prefill.email", "customer@example.com");
            options.put("prefill.contact", "1234567890");
            checkout.open(viewcart.this, options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String paymentNumericId) {
        Toast.makeText(viewcart.this, "Payment Successful. Payment ID: " + paymentNumericId, Toast.LENGTH_SHORT).show();

        // Store payment details in Firebase
        storePaymentDetailsInFirebase(paymentNumericId);
        recreate();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }

    private void storePaymentDetailsInFirebase(String razorpayPaymentID) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String paymentDate = dateFormat.format(new Date());
        String paymentStatus = "pending";

        DatabaseReference paymentsRef = FirebaseDatabase.getInstance().getReference().child("payments");
        String paymentNumericId = String.valueOf(generateUniqueNumericId()); // Generate a unique numeric ID

        // Use the payment ID as the key
        String paymentKey = String.valueOf(paymentNumericId);

        Map<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put("paymentId", paymentNumericId); // Use the generated numeric ID
        paymentDetails.put("amount", amount);
        paymentDetails.put("paymentDate", paymentDate);
        paymentDetails.put("paymentStatus", paymentStatus);

        paymentsRef.child(paymentKey).setValue(paymentDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "Payment details added successfully!");
                        storeItemDetailsUnderPayment(paymentKey, paymentNumericId); // Pass paymentKey and numeric ID
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Failed to add payment details: " + e.getMessage());
                    }
                });
    }




    private void storeItemDetailsUnderPayment(String paymentKey, String paymentNumericId) {
        DatabaseReference paymentsRef = FirebaseDatabase.getInstance().getReference().child("payments").child(paymentKey).child("items");

        for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
            String[] parts = cartSnapshot.getValue(String.class).split("\\s+");
            final String name = parts[1];
            final String quantity = parts[3];
            final String price = parts[5];

            Map<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("name", name);
            itemDetails.put("quantity", quantity);
            itemDetails.put("price", price);
            itemDetails.put("total", Integer.parseInt(price) * Integer.parseInt(quantity));

            paymentsRef.push().setValue(itemDetails)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase", "Item details added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Failed to add item details: " + e.getMessage());
                        }
                    });
        }

        // Remove cart details after completing payment
        cartRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "Cart details removed successfully!");
                        cartEmptyTextView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Failed to remove cart details: " + e.getMessage());
                    }
                });
    }


    private int generateUniqueNumericId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyM   MddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());
        Random random = new Random();
        int randomNum = random.nextInt(90000) + 10000;
        return randomNum;
    }
}
