package com.example.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity2 extends AppCompatActivity {

    TextView profileName, profileEmail, profileShopname, profileAddress, profilePassword;
    TextView titleName, titleShopname; // Add this line
    Button editProfile;
    ImageView profileImage;
    private String username;

    private static final int EDIT_PROFILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        profileName = findViewById(R.id.profileName2);
        profileEmail = findViewById(R.id.profileEmail2);
        profileShopname = findViewById(R.id.profileShopname2);
        profileAddress = findViewById(R.id.profileAddress2);
        profilePassword = findViewById(R.id.profilePassword2);
        titleName = findViewById(R.id.titleName2);
        titleShopname = findViewById(R.id.profileShopname2); // Add this line
        editProfile = findViewById(R.id.editButton2);
        profileImage = findViewById(R.id.profileImg2);

        UserData userData = UserData.getInstance();
        username = userData.getUsername();

        showShopData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditProfileActivity();
            }
        });
    }

    private void showShopData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");
        Query checkShopDatabase = reference.orderByChild("username").equalTo(username);

        checkShopDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot shopSnapshot = snapshot.getChildren().iterator().next();

                    String emailFromDB = shopSnapshot.child("email").getValue(String.class);
                    String passwordFromDB = shopSnapshot.child("password").getValue(String.class);
                    String addressFromDB = shopSnapshot.child("address").getValue(String.class);
                    String imageFromDB = shopSnapshot.child("imageURL").getValue(String.class);
                    String shopName = shopSnapshot.child("shopname").getValue(String.class);

                    titleName.setText(username);
                    profileName.setText(username);
                    profileEmail.setText(emailFromDB);
                    titleShopname.setText(shopName);
                    profileAddress.setText(addressFromDB);
                    profilePassword.setText(passwordFromDB);

                    Picasso.get().load(imageFromDB).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

    private void startEditProfileActivity() {
        Intent intent = new Intent(ProfileActivity2.this, EditProfileActivity2.class);
        intent.putExtra("name", username);
        intent.putExtra("email", profileEmail.getText().toString());
        intent.putExtra("shopname", titleShopname.getText().toString());
        intent.putExtra("address", profileAddress.getText().toString());
        intent.putExtra("password", profilePassword.getText().toString());

        startActivityForResult(intent, EDIT_PROFILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            showShopData();
        }
    }
}
