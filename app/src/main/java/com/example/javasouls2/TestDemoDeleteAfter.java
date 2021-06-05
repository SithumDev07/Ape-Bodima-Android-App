package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDemoDeleteAfter extends AppCompatActivity implements DatabaseCredentials{

    Button click;
    TextView label;

    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_demo_delete_after);

        click = (Button) findViewById(R.id.ClickBtn);
        label = (TextView) findViewById(R.id.ChangeText);

        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");


    }

    /*public void ChangeMode(View view){

        try{

            connection = connectionclass();

            if(connection == null){
                label.setText("failed");
            }
            else{
                label.setText("Success");
            }

        }
        catch (Exception e){
            Log.e("Error here 1: ", e.getMessage());
        }

    }*/

    public class CheckLogin extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {

            String text = label.getText().toString();

            if(!text.equals("Pass2")){
                z = "Passwords are not match";
            }
            else{

                if(text.equals("")){

                    z = "Please enter valid username and password";

                }
                else{
                    try {
                        connection = connectionclass(User,Pass,DB,IP,PORT);

                        if(connection == null){
                            label.setText("Failed");
                        }
                        else{
                            label.setText("Success");
                        }
                        isSuccess = true;
                        connection.close();
                    }
                    catch (Exception e){
                        isSuccess = false;
                        z = e.getMessage();
                    }
                }

            }

        }

        @Override
        protected void onPostExecute(String r)
        {
        }

        @Override
        protected String doInBackground(String... params) {

            String z = "";

            return z;

        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server, String port){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");


            //ConnectionURL = "jdbc:jtds:sqlserver://apebodima.database.windows.net:1433;DatabaseName=ApeBodima;user=adminApeBodima@apebodima;password=Fr13nds2552;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";


            con = DriverManager.getConnection(ConnectionURLS,UserD,PassD);

        }
        catch (SQLException se){
            Log.e("Error here 1: ", se.getMessage());
        }
        catch (ClassNotFoundException e){
            Log.e("Error here 2: ", e.getMessage());
        }
        catch (Exception e){
            Log.e("Error here 3: ", e.getMessage());
        }
        return con;
    }


}