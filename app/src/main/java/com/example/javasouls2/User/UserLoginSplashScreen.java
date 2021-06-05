package com.example.javasouls2.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.javasouls2.CreateAccountAcitivity1;
import com.example.javasouls2.LoginActivity;
import com.example.javasouls2.R;

public class UserLoginSplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_login_splash_screen);

        //Animations Hooks
        topAnim = AnimationUtils.loadAnimation(this, R.anim.user_splash_animation_top);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.user_splash_animation_bottom);

        image = (ImageView) findViewById(R.id.mainLogoImage);
        logo = (TextView) findViewById(R.id.logoTitle);
        slogan = (TextView) findViewById(R.id.sloganText);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                Pair[] pairs = new Pair[2];

                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logo,"logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserLoginSplashScreen.this,pairs);
                startActivity(intent,options.toBundle());
            }
        },SPLASH_SCREEN);
    }
}