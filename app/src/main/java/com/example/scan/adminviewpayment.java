package com.example.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Map;

public class adminviewpayment extends AppCompatActivity {

    LinearLayout coversContainer;
    String PaymentId;
    TextView amountview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminviewpayments);
        amountview =findViewById(R.id.amountTextView);

        coversContainer = findViewById(R.id.coversContainer); // Make sure this ID matches the one in your layout XML file
        Intent intent = getIntent();

        PaymentId = intent.getStringExtra("paymentId");
        Log.d("Covers", "Clicked Payment ID: " + PaymentId);
        // Show all covers for the specific shop
        showCoversForShop();

        // Set OnClickListener for Complete Button
        Button completeButton = findViewById(R.id.CompleteButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePaymentStatus();
                movePaymentToHistory();
            }
        });
    }

    private void showCoversForShop() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("payments").child(PaymentId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("Covers", "No payments found in database");
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
                    View itemLayout = getLayoutInflater().inflate(R.layout.activity_adminitempaymentsdetails, coversContainer, false);

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

            }// Existing code to display payment details


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Covers", "Database error: " + error.getMessage());
            }
        });
    }

    private void updatePaymentStatus() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("payments").child(PaymentId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("Update Status", "Payment not found in database");
                    TextView noOffersTextView = new TextView(adminviewpayment.this);
                    noOffersTextView.setText("No Details available");
                    coversContainer.addView(noOffersTextView);

                    return;
                }

                // Update payment status to "completed"
                reference.child("paymentStatus").setValue("completed")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Update Status", "Payment status updated to completed");
                                // Optionally, you can also update your UI to reflect the change
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Update Status", "Failed to update payment status: " + e.getMessage());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Update Status", "Database error: " + error.getMessage());
            }
        });
    }

    private void movePaymentToHistory() {
        DatabaseReference paymentReference = FirebaseDatabase.getInstance().getReference("payments").child(PaymentId);
        DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference("payment_history");

        // Update payment status to "Completed"
        paymentReference.child("paymentStatus").setValue("Completed")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Update Status", "Payment status updated to Completed");

                        // Move payment to history
                        paymentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Map<String, Object> paymentDetails = (Map<String, Object>) snapshot.getValue();
                                    if (paymentDetails != null) {
                                        String paymentId = (String) paymentDetails.get("paymentId");
                                        paymentDetails.put("paymentStatus", "Completed");

                                        historyReference.child(paymentId).setValue(paymentDetails)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Move to History", "Payment moved to history successfully");
                                                        // Remove payment from "payments" node
                                                        paymentReference.removeValue();
                                                        Log.d("Remove Payment", "Payment removed from payments node");

                                                        // Show success message
                                                        showMessage("Payment completed and moved to history.");

                                                        // Finish current activity to reload adminpaymentdetails page
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("Move to History", "Failed to move payment to history: " + e.getMessage());
                                                        // Show error message
                                                        showMessage("Failed to move payment to history: " + e.getMessage());
                                                    }
                                                });
                                    } else {
                                        Log.e("Move to History", "Payment details are null");
                                        // Show error message
                                        showMessage("Failed to move payment to history: Payment details are null");
                                    }
                                } else {
                                    Log.e("Move to History", "Payment not found in database");
                                    // Show error message
                                    showMessage("Failed to move payment to history: Payment not found in database");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Move to History", "Database error: " + error.getMessage());
                                // Show error message
                                showMessage("Database error: " + error.getMessage());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Update Status", "Failed to update payment status: " + e.getMessage());
                        // Show error message
                        showMessage("Failed to update payment status: " + e.getMessage());
                    }
                });
    }

    // Method to show messages
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
