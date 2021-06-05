package com.example.javasouls2.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.javasouls2.ClientLoginWindow;
import com.example.javasouls2.ClientPanel;
import com.example.javasouls2.LoginActivity;
import com.example.javasouls2.R;
import com.example.javasouls2.SearchMenu;
import com.example.javasouls2.SplashScreen;

public class SplashScreenAfterAnnex extends AppCompatActivity {

    ImageView image;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen_after_annex);

        image = (ImageView) findViewById(R.id.PostCreateSplashLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SearchMenu.class);

                startActivity(intent);
                finish();

            }
        },SPLASH_SCREEN);
    }
}