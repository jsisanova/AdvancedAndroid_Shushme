package com.example.android.shushme;

import com.google.android.gms.maps.model.LatLng;

public class MyPlace {
    private String id, name, address;
    private LatLng latLng;

    public MyPlace(String id, String name, String address, LatLng latLng ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}