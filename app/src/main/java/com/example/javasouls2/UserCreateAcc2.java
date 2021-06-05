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

import com.example.javasouls2.User.DataValidationUser;
import com.example.javasouls2.User.InputValidationUser;
import com.example.javasouls2.User.UserPrelogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class UserCreateAcc2 extends AppCompatActivity implements DatabaseCredentials{

    //Variables
    private ImageView mainLogo;
    private TextView Logo;
    private TextView slogan;

    private EditText phoneNumber;
    private EditText guardianPhone;
    private EditText Email;

    private String GenderV;
    private RadioButton Male;
    private RadioButton Female;

    private Button Finish;
    private Button logBack;

    private Connection connection;
    private Statement statement;

    private String passingID;
    public static final String EXTRA_TEXT = "com.example.javasouls2.UserCreateAcc2.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_create_acc2);

        //Hooks
        mainLogo = (ImageView) findViewById(R.id.LogoImage);
        Logo = (TextView) findViewById(R.id.LogoName);
        slogan = (TextView) findViewById(R.id.slogan_name);

        phoneNumber = (EditText) findViewById(R.id.PhoneNumberInput);
        guardianPhone = (EditText) findViewById(R.id.GuardianPhoneInput);
        Email = (EditText) findViewById(R.id.EmailAddressInput);
        Male = (RadioButton) findViewById(R.id.MaleChecked);
        Female = (RadioButton) findViewById(R.id.FemaleChecked);
        GenderV = "O";

        Finish = (Button) findViewById(R.id.FinishBtn);
        logBack = (Button) findViewById(R.id.LogBackBtn);

        SessionManager sessionManager = new SessionManager(UserCreateAcc2.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

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

        InputValidationUser inputValidationUser = new InputValidationUser(phoneNumber,Email);

        DataValidationUser dataValidationUser = new DataValidationUser(phoneNumber,Email);

        if (!inputValidationUser.validPhoneNumber(phoneNumber) | !inputValidationUser.validEmail() | !inputValidationUser.validPhoneNumber(guardianPhone) | dataValidationUser.GetValidationPhone()){
            return;
        }
        else{
            CheckLogin checkLogin = new CheckLogin();
            checkLogin.execute("");
        }
    }

    public void moveLogBack(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        Pair[] pairs = new Pair[7];

        pairs[0] = new Pair<View, String>(mainLogo, "logo_image");
        pairs[1] = new Pair<View, String>(Logo, "logo_text");
        pairs[2] = new Pair<View, String>(slogan, "slogan_text");
        pairs[3] = new Pair<View, String>(phoneNumber, "username_");
        pairs[4] = new Pair<View, String>(guardianPhone, "password_");
        pairs[5] = new Pair<View, String>(Finish, "Next");
        pairs[6] = new Pair<View, String>(logBack, "logback");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserCreateAcc2.this, pairs);
        startActivity(intent, options.toBundle());
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
            Toast.makeText(UserCreateAcc2.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(UserCreateAcc2.this , "One More step!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserCreateAcc3.class);

                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(mainLogo, "logo_image");
                pairs[1] = new Pair<View, String>(Logo, "logo_text");
                pairs[2] = new Pair<View, String>(slogan, "slogan_text");
                pairs[3] = new Pair<View, String>(phoneNumber, "username_");
                pairs[4] = new Pair<View, String>(guardianPhone, "SecondInput");
                pairs[5] = new Pair<View, String>(Finish, "Next");
                pairs[6] = new Pair<View, String>(logBack, "logback");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserCreateAcc2.this, pairs);

                startActivity(intent,options.toBundle());
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String Phone = phoneNumber.getText().toString();
            String Home = guardianPhone.getText().toString();
            String eMail = Email.getText().toString();


            if(Phone.trim().equals("") || Home.trim().equals("")){

                z = "Please enter valid username and password";

            }
            else{
                try {
                    connection = connectionclass(User,Pass,DB,IP,PORT);

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "UPDATE AppUser SET PhoneNumber = '" + Phone + "',GuardianPhoneNumber = '" + Home + "',UserEmail = '" + eMail + "',Gender = '" + GenderV + "' WHERE Username = '" + passingID + "';";
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