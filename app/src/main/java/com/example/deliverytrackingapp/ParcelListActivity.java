package com.example.deliverytrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import androidx.core.content.ContextCompat;

public class ParcelListActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    private ParcelAdapter parcelAdapter;
    private String status;
    private String userId;
    private String parcelCount;
    private List<Parcel> parcelList;

    @Override
    public void onItemClick(Parcel parcel) {
        // Create an intent to start ParcelDetailActivity
        Intent intent = new Intent(this, ParcelDetailActivity.class);

        // Pass the clicked parcel's data to ParcelDetailActivity using extras
        intent.putExtra("parcel", parcel);

        // Start the activity
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_list);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Retrieve parcel data based on user ID and desired status (e.g., "success")
        userId = getIntent().getStringExtra("userId");
        status = getIntent().getStringExtra("status");
        parcelCount = getIntent().getStringExtra("parcelCount");

        setCustomActionBar();

        // Initialize the parcelList
        parcelList = new ArrayList<>();

        // Create the ParcelAdapter (initially empty)
        parcelAdapter = new ParcelAdapter(ParcelListActivity.this, parcelList);
        parcelAdapter.setItemClickListener(this); // Make sure ParcelListActivity implements ItemClickListener


        // Set the RecyclerView's adapter and layout manager
        recyclerView.setAdapter(parcelAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ParcelListActivity.this));

        retrieveParcelDataBasedOnStatus();
    }

    private void setCustomActionBar() {
        // Hide the default ActionBar
//        getSupportActionBar().hide();

        // Find custom ActionBar views
        ImageButton backButton = findViewById(R.id.backButton);
        TextView titleTextView = findViewById(R.id.titleTextView);

        // Set the title text
        titleTextView.setText(status + " (" + parcelCount + ")");

        int actionBarColor = 0;
        if (status != null) {
            if (status.equals("pending")) {
                actionBarColor = ContextCompat.getColor(this, R.color.colorPendingParcels);
            } else if (status.equals("failed")) {
                actionBarColor = ContextCompat.getColor(this, R.color.colorFailedParcels);
            } else if (status.equals("success")) {
                actionBarColor = ContextCompat.getColor(this, R.color.colorSuccessParcels);
            } else {
                // Default color if status doesn't match any of the predefined values
                actionBarColor = ContextCompat.getColor(this, R.color.black);
            }
        }
        // Find the actionBarLayout and set its background color
        LinearLayout actionBarLayout = findViewById(R.id.actionBarLayout);
        actionBarLayout.setBackgroundColor(actionBarColor);

        // Handle the back button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click (e.g., navigate back to MainActivity)
                onBackPressed();
            }
        });
    }

    private void retrieveParcelDataBasedOnStatus() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("deliveries")
                .child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parcelList.clear(); // Clear the list to avoid duplicating data

                for (DataSnapshot deliverySnapshot : dataSnapshot.getChildren()) {
                    Parcel parcel = deliverySnapshot.getValue(Parcel.class);
                    if (parcel != null && status.equals(parcel.getStatus())) {
                        parcelList.add(parcel);
                    }
                }

                // Notify the adapter that the data has changed
                parcelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }


}
