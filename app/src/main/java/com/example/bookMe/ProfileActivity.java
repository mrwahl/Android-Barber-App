package com.example.bookMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    // declare some variables
    private TextView firstName, secondName, email;
    private String fName, sName, userEmail;
    private FirebaseAuth authProfile;
    private Button bookingBtn, logoutBtn, viewBookingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.fnameText);
        secondName = findViewById(R.id.sNameText);
        email = findViewById(R.id.emailText);
        bookingBtn = findViewById(R.id.bookingButton);
        logoutBtn = findViewById(R.id.logoutButton);
        viewBookingsBtn = findViewById(R.id.viewBookingsButton);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(ProfileActivity.this, "Error!", Toast.LENGTH_LONG).show();
        } else {
            showUserProfile(firebaseUser);
        }

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingIntent();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutIntent();
            }
        });
        viewBookingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBookings();
            }
        });

    }

    //view bookings intent
    private void viewBookings() {
        Intent intent = new Intent(ProfileActivity.this, BookingList.class);
        Toast.makeText(ProfileActivity.this, "Viewing bookings. . .", Toast.LENGTH_SHORT).show();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //logout out of app intent
    private void logoutIntent() {
        authProfile.signOut();
        Toast.makeText(ProfileActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // go to booking page intent
    private void bookingIntent() {
        Intent intent = new Intent(ProfileActivity.this, BookingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //method to display the currently logged in user
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        //Extract user reference from database for "Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User thisUser = snapshot.getValue(User.class);
                if (thisUser != null) {
                    userEmail = firebaseUser.getEmail();
                    fName = thisUser.firstName;
                    sName = thisUser.secondName;
                    // Set the text views based on the data
                    firstName.setText(fName);
                    secondName.setText(sName);
                    email.setText(userEmail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}