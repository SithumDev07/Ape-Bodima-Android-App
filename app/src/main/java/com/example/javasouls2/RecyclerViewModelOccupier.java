package com.example.javasouls2;

public class RecyclerViewModelOccupier {

    private int image;
    private String Name;
    private String age;
    private String phoneNumber;
    private String since;
    private String OccupierID;
    private String PlaceID;

    public RecyclerViewModelOccupier(int image, String name, String age, String phoneNumber, String since,String OccupierID,String PlaceID) {
        this.image = image;
        Name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.since = since;
        this.OccupierID = OccupierID;
        this.PlaceID = PlaceID;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return Name;
    }

    public String getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSince() {
        return since;
    }

    public String getOccupierID() {
        return OccupierID;
    }

    public String getPlaceID() {
        return PlaceID;
    }
}
