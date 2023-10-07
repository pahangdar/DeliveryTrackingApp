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

    public Parcel() {
        // Default constructor required by Firebase
    }

    public Parcel(String deliveryId, int sequenceNumber, String customerName, String address,
                  String phoneNumber, String status, double latitude, double longitude) {
        this.deliveryId = deliveryId;
        this.sequenceNumber = sequenceNumber;
        this.customerName = customerName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    // Constructors, getters, and setters
}
