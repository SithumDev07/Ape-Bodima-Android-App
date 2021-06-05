package com.example.javasouls2;

import android.graphics.Bitmap;
import android.widget.RatingBar;

public class StaticRecyclerViewModel {

    private int image;
    private String text;
    private String rooms;
    private String bathrooms;
    private String payment;
    private float rating;
    private String BoardingID;
    private String RoomID;

    public StaticRecyclerViewModel(int image, String text, String rooms, String bathrooms, String payment, float rating, String BoardingID,String RoomID) {
        this.image = image;
        this.text = text;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.payment = payment;
        this.rating = rating;
        this.BoardingID = BoardingID;
        this.RoomID = RoomID;
    }


    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public String getRooms() {
        return rooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public String getPayment() {
        return payment;
    }

    public float getRating() {
        return rating;
    }

    public String getBoardingID(){
        return BoardingID;
    }

    public String getRoomID() {
        return RoomID;
    }
}
