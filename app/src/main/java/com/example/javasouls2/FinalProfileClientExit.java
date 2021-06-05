package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.ImageButton;

public class FinalProfileClientExit extends AppCompatActivity {

    ImageButton EditAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_profile_client_exit);

        EditAcc = (ImageButton) findViewById(R.id.editAccountButton);

        EditAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalProfileClientExit.this, EditAccountWindow.class);
                startActivity(intent);
            }
        });
    }
}