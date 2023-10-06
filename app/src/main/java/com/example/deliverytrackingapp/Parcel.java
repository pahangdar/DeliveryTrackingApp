package com.example.deliverytrackingapp;
import java.io.Serializable;

public class Parcel implements Serializable {
    private String deliveryId;
    private int sequenceNumber;
    private String customerName;
    private String address;
    private String phoneNumber;
    private String status;
    private double latitude;
    private double longitude;

    public String getDeliveryId() {
        return deliveryId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Constructors, getters, and setters
}
