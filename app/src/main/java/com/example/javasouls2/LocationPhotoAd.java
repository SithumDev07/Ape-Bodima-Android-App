package com.example.javasouls2;

import android.graphics.Bitmap;

public class LocationPhotoAd {

   // public String title, ImageUrl;
    //public int image;
    public Bitmap bitmap;

    public LocationPhotoAd(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
