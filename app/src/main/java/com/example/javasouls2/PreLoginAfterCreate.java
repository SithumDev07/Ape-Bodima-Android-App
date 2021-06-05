package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PreLoginAfterCreate extends AppCompatActivity {

    ImageView ClientProfile;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login_after_create);

        back = (Button) findViewById(R.id.backToLogin);
        ClientProfile = (ImageView) findViewById(R.id.ClientDp);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreLoginAfterCreate.this,ClientLoginWindow.class);
                startActivity(intent);
            }
        });
    }
}