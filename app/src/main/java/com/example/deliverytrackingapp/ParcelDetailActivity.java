package com.example.deliverytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.Manifest;


public class ParcelDetailActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST_CODE = 123; // You can choose any integer value
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 456;

    private TextView sequenceNumberTextView;
    private TextView customerNameTextView;
    private TextView addressTextView;
    private TextView phoneNumberTextView;
    private ImageButton backButton;
    private ImageButton callButton;
    private ImageButton navButton;
    private String phoneNumber;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_detail);

        sequenceNumberTextView = findViewById(R.id.sequenceNumberTextView);
        customerNameTextView = findViewById(R.id.customerNameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        backButton = findViewById(R.id.backButton);
        callButton = findViewById(R.id.callButton);
        navButton = findViewById(R.id.navButton);

        // Retrieve parcel data from intent extras or wherever it's available
        Parcel parcel = (Parcel) getIntent().getSerializableExtra("parcel");

        if (parcel != null) {
            // Set text for each TextView with parcel data
            sequenceNumberTextView.setText("Sequence Number: " + parcel.getSequenceNumber());
            customerNameTextView.setText("Customer Name: " + parcel.getCustomerName());
            addressTextView.setText("Address: " + parcel.getAddress());
            phoneNumberTextView.setText("Phone Number: " + parcel.getPhoneNumber());

            phoneNumber = parcel.getPhoneNumber();
            latitude = parcel.getLatitude();
            longitude = parcel.getLongitude();
        }

        checkLocationPermission();

        // Set a click listener for the backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the back button click (e.g., navigate back to ParcelListActivity)
                onBackPressed();
            }
        });


        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the CALL_PHONE permission is granted
                if (ContextCompat.checkSelfPermission(ParcelDetailActivity.this, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is already granted, so make the phone call
                    makePhoneCall(phoneNumber);
                } else {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(ParcelDetailActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PERMISSION_REQUEST_CODE);
                }
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check location permission
                if (ContextCompat.checkSelfPermission(ParcelDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, start navigation
                    startNavigation(latitude, longitude);
                } else {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(ParcelDetailActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        });

    }

    private void makePhoneCall(String phoneNumber) {
        // Check if the CALL_PHONE permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Create an Intent to initiate the phone call
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
                // Handle the exception, e.g., display an error message to the user
            }
        } else {
            // Request the CALL_PHONE permission if it is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        }
    }

    // Check for location permissions
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, proceed with navigation
            startNavigation(latitude, longitude);
        }
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            // This is the result for the CALL_PHONE permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can make the phone call here
                makePhoneCall(phoneNumber);
            } else {
                // Permission denied, handle it (e.g., show a message)
            }
        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // This is the result for the location permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can start navigation here
                startNavigation(latitude, longitude);
            } else {
                // Permission denied, handle it (e.g., show a message)
            }
        }
    }

    private void startNavigation(double latitude, double longitude) {
        try {
            // Create an intent with the action to start navigation
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps"); // Specify the navigation app package

            // Check if there is a navigation app available
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent); // Start navigation
            } else {
                // Handle the case where no navigation app is available
                // You can show a message to the user or provide an alternative action
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur during navigation
        }
    }



}