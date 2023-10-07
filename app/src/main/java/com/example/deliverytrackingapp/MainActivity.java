package com.example.deliverytrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private LinearLayout totalParcelsLayout;
    private LinearLayout pendingParcelsLayout;
    private LinearLayout failedParcelsLayout;
    private LinearLayout successParcelsLayout;

    private TextView totalParcelsTextView;
    private TextView pendingParcelsTextView;
    private TextView failedParcelsTextView;
    private TextView successParcelsTextView;
    private DatabaseReference databaseRef;
    private int totalCount;
    private int pendingCount;
    private int failedCount;
    private int successCount;

    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the user ID from Intent extras
        userId = getIntent().getStringExtra("userId");

        // Initialize UI elements
        totalParcelsLayout = findViewById(R.id.totalParcelsLayout);
        pendingParcelsLayout = findViewById(R.id.pendingParcelsLayout);
        failedParcelsLayout = findViewById(R.id.failedParcelsLayout);
        successParcelsLayout = findViewById(R.id.successParcelsLayout);

        totalParcelsTextView = findViewById(R.id.totalParcelsTextView);
        pendingParcelsTextView = findViewById(R.id.pendingParcelsTextView);
        failedParcelsTextView = findViewById(R.id.failedParcelsTextView);
        successParcelsTextView = findViewById(R.id.successParcelsTextView);

        // Initialize Firebase Realtime Database reference based on the user ID
        databaseRef = FirebaseDatabase.getInstance().getReference().child("deliveries").child(userId);

        // Retrieve parcel data from Firebase Realtime Database
//        retrieveParcelData();

        // Set click listeners for rectangles
        setRectangleClickListener(pendingParcelsLayout, "pending");
        setRectangleClickListener(failedParcelsLayout, "failed");
        setRectangleClickListener(successParcelsLayout, "success");

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateParcelCounts(); // Refresh the parcel counts when the activity is resumed
    }

    private void setRectangleClickListener(final LinearLayout layout, final String status) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click event for the rectangle
                // We can navigate to another page based on the 'status' parameter
                Intent intent = new Intent(MainActivity.this, ParcelListActivity.class);
                intent.putExtra("status", status);
                intent.putExtra("userId", userId);
                if("pending".equals(status)) {
                    intent.putExtra("parcelCount", "" + pendingCount);
                } else if("failed".equals(status)) {
                    intent.putExtra("parcelCount", "" + failedCount);
                } else if ("success".equals(status)) {
                    intent.putExtra("parcelCount", "" + successCount);
                } else {
                    intent.putExtra("parcelCount", "");
                }

                startActivity(intent);
            }
        });
    }

    private void updateParcelCounts() {
        // Listen for changes in the database at the specified path
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize counts
                int totalParcels = 0;
                int pendingParcels = 0;
                int failedParcels = 0;
                int successParcels = 0;

                // Iterate through delivery records for specific driver (user)
                for (DataSnapshot parcelSnapshot : dataSnapshot.getChildren()) {
                    // Get the status for each parcel
                    String status = parcelSnapshot.child("status").getValue(String.class);

                    // Update counts based on status
                    totalParcels++;
                    if ("pending".equals(status)) {
                        pendingParcels++;
                    } else if ("failed".equals(status)) {
                        failedParcels++;
                    } else if ("success".equals(status)) {
                        successParcels++;
                    }
                }

                // Update the TextViews with the parcel counts
                totalParcelsTextView.setText("Total Parcels: " + totalParcels);
                pendingParcelsTextView.setText("Pending Parcels: " + pendingParcels);
                failedParcelsTextView.setText("Failed Parcels: " + failedParcels);
                successParcelsTextView.setText("Successful Parcels: " + successParcels);

                totalCount = totalParcels;
                pendingCount = pendingParcels;
                failedCount = failedParcels;
                successCount = successParcels;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }



}
