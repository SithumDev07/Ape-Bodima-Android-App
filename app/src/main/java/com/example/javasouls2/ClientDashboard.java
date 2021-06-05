package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ClientDashboard extends AppCompatActivity {

    ImageButton bigProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        bigProfileBtn  = (ImageButton) findViewById(R.id.imageButton);

        bigProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientDashboard.this,FinalProfileClientExit.class);
                startActivity(intent);
            }
        });
    }
}