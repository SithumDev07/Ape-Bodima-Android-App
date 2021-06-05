package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.javasouls2.Client.AddBoardingHouseRoomsWindow;

public class AddedBoardingSplash extends AppCompatActivity {

    ImageView image;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_added_boarding_splash);

        image = (ImageView) findViewById(R.id.PostCreateSplashLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), AddBoardingHouseRoomsWindow.class);

                startActivity(intent);
                finish();

            }
        },SPLASH_SCREEN);
    }
}