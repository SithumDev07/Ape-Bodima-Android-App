package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.example.javasouls2.User.FilterAndSearchMenuUser;
import com.example.javasouls2.User.UserLoginSplashScreen;


public class MainActivity extends AppCompatActivity{

    private Button user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        user = (Button) findViewById(R.id.UserBtn);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, UserLoginSplashScreen.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {

        finish();
        System.exit(0);

    }

    public void MoveToCheckImage(View view){

        Intent intent = new Intent(getApplicationContext(),SplashScreen.class);
        startActivity(intent);

    }

    public void callLoginScreen(View view){
        Intent intent = new Intent(getApplicationContext(),ClientLoginWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View,String>(findViewById(R.id.ClientBtn),"transition_client");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);

        startActivity(intent,options.toBundle());
    }
}