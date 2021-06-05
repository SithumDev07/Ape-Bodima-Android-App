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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ClientCreatePt2 extends AppCompatActivity implements ClientCommandInterface, DatabaseCredentials{

    private EditText phoneNumber;
    private EditText homeMobile;
    private RadioButton Male;
    private String GenderV;
    private RadioButton Female;
    private EditText Email;
    private String passingID;

    private ImageView backBtn;
    private TextView Title;


    private Button clientNext,LogBack;

    private Connection connection;
    private Statement statement;

    public static final String EXTRA_TEXT = "com.example.javasouls2.ClientCreatePt2.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_create_pt2);

        phoneNumber = (EditText) findViewById(R.id.MainPhoneNumber);
        homeMobile = (EditText) findViewById(R.id.SecondaryPhoneNumber);
        Male = (RadioButton) findViewById(R.id.MaleChecked);
        Female = (RadioButton) findViewById(R.id.FemaleChecked);
        Email = (EditText) findViewById(R.id.EmailAddress);

        GenderV = "O";

        backBtn = (ImageView) findViewById(R.id.create_Acc_backButton);
        Title = (TextView) findViewById(R.id.CreateAccTextTitle);

        clientNext = (Button) findViewById(R.id.signupNextButton);
        LogBack = (Button) findViewById(R.id.BackToLoginButton);

        SessionManager sessionManager = new SessionManager(ClientCreatePt2.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        Male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    GenderV = "M";
                }
            }
        });

        Female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    GenderV = "F";
                }
            }
        });

    }

    public void CallNextWindow(View view) throws SQLException {

        InputValidationClient inputValidation = new InputValidationClient(Email);

        DataValidationClient dataValidationClient = new DataValidationClient(phoneNumber,Email);

        if(!inputValidation.validEmail() | !inputValidation.validPhoneNumber(phoneNumber) | !inputValidation.validPhoneNumber(homeMobile) | dataValidationClient.GetValidationPhoneNumber()){
            return;
        }
        else{
            CheckLogin checkLogin = new CheckLogin();
            checkLogin.execute("");
        }
    }

    public void callBackLoginWindow(View view){
        Intent intent = new Intent(getApplicationContext(),ClientLoginWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View,String>(findViewById(R.id.BackToLoginButton),"transition_client");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreatePt2.this,pairs);

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
            Toast.makeText(ClientCreatePt2.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(ClientCreatePt2.this , "Just one more step" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ClientCreatePt3.class);

                //Animation

                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(backBtn,"animation_back_btn");
                pairs[1] = new Pair<View,String>(clientNext,"animation_next_btn");
                pairs[2] = new Pair<View,String>(LogBack,"transition_client");
                pairs[3] = new Pair<View,String>(Title,"animation_title_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreatePt2.this,pairs);
                startActivity(intent,options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String Phone = phoneNumber.getText().toString();
            String Home = homeMobile.getText().toString();
            String eMail = Email.getText().toString();

            if(eMail.equals("")){
                eMail = "@apebodima.com";
            }


            if(Phone.trim().equals("") || GenderV.trim().equals("")){

                z = "Please enter valid username and password";

            }
            else{
                try {
                    connection = connectionclass(User,Pass,DB,IP,PORT);

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "UPDATE Client SET Gender = '" + GenderV + "',Email = '" + eMail + "' WHERE NIC = '" + passingID + "';";
                        String QueryPhoneNumber = "INSERT INTO ClientPhoneNumber (NIC,PhoneNumber) values ('" + passingID + "','" + Phone + "')";
                        String QueryPhoneNumberHome = "INSERT INTO ClientPhoneNumber (NIC,PhoneNumber) values ('" + passingID + "','" + Home + "')";
                        statement = connection.createStatement();
                        statement.executeUpdate(query);
                        statement.executeUpdate(QueryPhoneNumber);
                        statement.executeUpdate(QueryPhoneNumberHome);

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