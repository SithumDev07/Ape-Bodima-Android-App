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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ClientCreatePt3 extends AppCompatActivity implements ClientCommandInterface, DatabaseCredentials{

    private EditText AddressLine1;
    private EditText AddressLine2;
    private EditText AddressCity;
    private EditText PostalCode;
    private String passingID;

    private ImageView backBtn;
    private TextView Title;

    private Button finishBtn,LogBack;

    private Connection connection;
    Statement statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_create_pt3);

        AddressLine1 = (EditText) findViewById(R.id.AddressLine1);
        AddressLine2 = (EditText) findViewById(R.id.AddressLine2);
        AddressCity = (EditText) findViewById(R.id.City);
        PostalCode = (EditText) findViewById(R.id.PostalCode);

        backBtn = (ImageView) findViewById(R.id.create_Acc_backButton);
        Title = (TextView) findViewById(R.id.CreateAccTextTitle);

        finishBtn = (Button) findViewById(R.id.signupFinish);
        LogBack = (Button) findViewById(R.id.BackToLoginButton);

        SessionManager sessionManager = new SessionManager(ClientCreatePt3.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

    }

    public void CallNextWindow(View view){
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");
    }

    public void callLoginback(View view){
        Intent intent = new Intent(getApplicationContext(),ClientLoginWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View,String>(findViewById(R.id.BackToLoginButton),"transition_client");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreatePt3.this,pairs);

        startActivity(intent,options.toBundle());
    }

    public class CheckLogin extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(ClientCreatePt3.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(ClientCreatePt3.this , "Hooray!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ClientCreatePt4.class);

                //Animation

                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(backBtn,"animation_back_btn");
                pairs[1] = new Pair<View,String>(finishBtn,"animation_next_btn");
                pairs[2] = new Pair<View,String>(LogBack,"transition_client");
                pairs[3] = new Pair<View,String>(Title,"animation_title_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreatePt3.this,pairs);
                startActivity(intent,options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String Line1 = AddressLine1.getText().toString();
            String Line2 = AddressLine2.getText().toString();
            String City = AddressCity.getText().toString();
            String Postal = PostalCode.getText().toString();


            if(Line1.trim().equals("") || City.trim().equals("") || Postal.trim().equals("")){

                z = "Please enter valid username and password";

            }
            else{
                try {
                    connection = connectionclass(User,Pass,DB,IP,PORT);

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "UPDATE Client SET AddressLine1 = '" + Line1 + "',Line2 = '" + Line2 + "',City = '" + City + "',PostalCode = '" + Postal + "' WHERE NIC = '" + passingID + "';";
                        statement = connection.createStatement();
                        statement.executeUpdate(query);
                    }
                    isSuccess = true;
                    connection.close();
                }
                catch (Exception e){
                    isSuccess = false;
                    z = e.getMessage();
                }
            }

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