package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;

public class UserEditAccountWindow extends AppCompatActivity {

    NeomorphFrameLayout Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_account_window);

        Logout = (NeomorphFrameLayout) findViewById(R.id.logOutBtn);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserEditAccountWindow.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}