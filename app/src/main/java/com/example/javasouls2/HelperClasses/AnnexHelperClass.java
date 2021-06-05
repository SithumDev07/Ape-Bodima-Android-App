package com.example.javasouls2.HelperClasses;

public class AnnexHelperClass {

    private int image;
    private String title;
    private String rooms;
    private String bathrooms;

    public AnnexHelperClass(int image, String title, String rooms, String bathrooms) {
        this.image = image;
        this.title = title;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getRooms() {
        return rooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }
}
