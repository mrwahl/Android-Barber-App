package com.example.bookMe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {
    //declare some variables
    EditText dateIn;
    Button profileButton, logoutButton;
    DatabaseReference rootDatabaseRef;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        //initialise them
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();
        dateIn = findViewById(R.id.selectDate);
        profileButton = findViewById(R.id.profileBtn);
        logoutButton = findViewById(R.id.logoutButton);

        // add " Bookings " under each user that is currently logged in with a randomly generated key or id
        rootDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Bookings");

        dateIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateIn);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileActivity();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutActivity();
            }
        });
    }

    // log out
    private void logoutActivity() {
        authProfile.signOut();
        Toast.makeText(BookingActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(BookingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // go to profile page
    private void profileActivity() {
        Intent intent = new Intent(BookingActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    //method to choose a date and time
    private void showDateTimeDialog(final EditText date_time_in) {
        //get an instance of the current local calender
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //set the dates
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //set the hour and minute
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        //format of the calender
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                        //add the booking in Firebase
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                        Bookings booking = new Bookings(year, month + 1, dayOfMonth, hourOfDay, minute);
                        rootDatabaseRef.push().setValue(booking);
                        Toast.makeText(BookingActivity.this, "Appointment successful ", Toast.LENGTH_SHORT).show();
                        date_time_in.setText(getString(R.string.select_a_date_and_timeText));
                    }
                };

                new TimePickerDialog(BookingActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

            }

        };

        new DatePickerDialog(BookingActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


}//end of class