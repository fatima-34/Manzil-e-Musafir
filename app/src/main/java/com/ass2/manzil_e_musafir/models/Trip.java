package com.ass2.manzil_e_musafir.models;

public class Trip {
    private String TID;
    private String place;
    private String price;
    private String description;
    private String location;
    private String details;
    private String itinerary;
    private String picturePath;

    public Trip(){

    }

    public Trip(String TID,String place, String price, String description, String location, String details, String itinerary, String picturePath) {
        this.TID = TID;
        this.place = place;
        this.price = price;
        this.description = description;
        this.location = location;
        this.details = details;
        this.itinerary = itinerary;
        this.picturePath = picturePath;
    }

    public String getTID() { return TID; }
    public void setTID(String TID) { this.TID = TID; }
    public String getPlace() { return place; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getDetails() { return details; }
    public String getItinerary() { return itinerary; }
    public String getPicturePath() { return picturePath; }

    public void setPlace(String place) { this.place = place; }
    public void setPrice(String price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setDetails(String details) { this.details = details; }
    public void setItinerary(String itinerary) { this.itinerary = itinerary; }
    public void setPicturePath(String picturePath) { this.picturePath = picturePath; }
}
