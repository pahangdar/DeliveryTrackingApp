package com.example.deliverytrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ParcelAdapter extends RecyclerView.Adapter<ParcelAdapter.ParcelViewHolder> {

    private List<Parcel> parcelList;
    private Context context;
    private ItemClickListener itemClickListener;

    public ParcelAdapter(Context context, List<Parcel> parcelList) {
        this.context = context;
        this.parcelList = parcelList;
    }

    // Setter method for the ItemClickListener
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parcel, parent, false);
        return new ParcelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelViewHolder holder, int position) {
        Parcel parcel = parcelList.get(position);

        // Bind parcel data to views
        holder.sequenceNumberTextView.setText("Sequence Number: " + parcel.getSequenceNumber());
        holder.customerNameTextView.setText("Customer Name: " + parcel.getCustomerName());
        holder.addressTextView.setText("Address: " + parcel.getAddress());
        holder.phoneNumberTextView.setText("Phone Number: " + parcel.getPhoneNumber());
        holder.statusTextView.setText("Status: " + parcel.getStatus());
        holder.latitudeTextView.setText("Latitude: " + parcel.getLatitude());
        holder.longitudeTextView.setText("Longitude: " + parcel.getLongitude());

        // Set an onClickListener for the entire item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call onItemClick method of the ItemClickListener with the clicked parcel
                itemClickListener.onItemClick(parcel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parcelList.size();
    }

    public class ParcelViewHolder extends RecyclerView.ViewHolder {
        TextView sequenceNumberTextView;
        TextView customerNameTextView;
        TextView addressTextView;
        TextView phoneNumberTextView;
        TextView statusTextView;
        TextView latitudeTextView;
        TextView longitudeTextView;

        public ParcelViewHolder(@NonNull View itemView) {
            super(itemView);
            sequenceNumberTextView = itemView.findViewById(R.id.sequenceNumberTextView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);
        }
    }
}
