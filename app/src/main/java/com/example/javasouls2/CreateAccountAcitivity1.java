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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasouls2.User.DataValidationUser;
import com.example.javasouls2.User.InputValidationUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateAccountAcitivity1 extends AppCompatActivity implements DatabaseCredentials {

    //Variables
    private ImageView mainLogo;
    private TextView Logo;
    private TextView slogan;

    private EditText username;
    private EditText firstName;
    private EditText lastName;
    private EditText password;

    private Button Next;
    private Button logBack;

    private Connection connection;

    public static final String EXTRA_TEXT = "com.example.javasouls2.CreateAccountActivity1.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create1);

        //Hooks
        mainLogo = (ImageView) findViewById(R.id.LogoImage);
        Logo = (TextView) findViewById(R.id.LogoName);
        slogan = (TextView) findViewById(R.id.slogan_name);

        username = (EditText) findViewById(R.id.UserInput);
        firstName = (EditText) findViewById(R.id.FirstnameInput);
        lastName = (EditText) findViewById(R.id.LastNameInput);
        password = (EditText) findViewById(R.id.PasswordInput);

        Next = (Button) findViewById(R.id.NextBtn);
        logBack = (Button) findViewById(R.id.CreateAccBtn);

    }


    public void CallNextWindowUser(View view) throws SQLException {

        InputValidationUser inputValidationUser = new InputValidationUser(username,firstName,lastName,password);
        DataValidationUser dataValidationUser = new DataValidationUser(username);

        if (!inputValidationUser.validUsername() | !inputValidationUser.validFirstName() | !inputValidationUser.validLastName() | !inputValidationUser.validPassword() | dataValidationUser.GetValidation()){

            return;

        }
        else {
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
        pairs[3] = new Pair<View, String>(username, "username_");
        pairs[4] = new Pair<View, String>(password, "password_");
        pairs[5] = new Pair<View, String>(Next, "Next");
        pairs[6] = new Pair<View, String>(logBack, "logback");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CreateAccountAcitivity1.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public class CheckLogin extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(CreateAccountAcitivity1.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(CreateAccountAcitivity1.this, "Data added", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserCreateAcc2.class);

                Pair[] pairs = new Pair[9];

                pairs[0] = new Pair<View, String>(mainLogo, "logo_image");
                pairs[1] = new Pair<View, String>(Logo, "logo_text");
                pairs[2] = new Pair<View, String>(slogan, "slogan_text");
                pairs[3] = new Pair<View, String>(username, "username_");
                pairs[4] = new Pair<View, String>(firstName, "SecondInput");
                pairs[5] = new Pair<View, String>(lastName, "ThirdInput");
                pairs[6] = new Pair<View, String>(password, "password_");
                pairs[7] = new Pair<View, String>(Next, "Next");
                pairs[8] = new Pair<View, String>(logBack, "logback");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CreateAccountAcitivity1.this, pairs);

                String passingID = username.getText().toString();
                startActivity(intent, options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String userID = username.getText().toString();
            String FirstName = firstName.getText().toString();
            String LastName = lastName.getText().toString();
            String Password = password.getText().toString();

            SessionManager sessionManager = new SessionManager(CreateAccountAcitivity1.this,SessionManager.SESSION_REMEMBERMEUSER);
            sessionManager.createRememberMeSessionUser(userID,Password);

            if (userID.trim().equals("") || FirstName.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "INSERT INTO AppUser(Username,FirstName,LastName,UserPassword) VALUES ('" + userID + "','" + FirstName + "','" + LastName + "','" + Password + "');";
                        Statement statement = connection.createStatement();

                        statement.executeUpdate(query);
                    }
                    isSuccess = true;
                    connection.close();
                } catch (Exception e) {
                    isSuccess = false;
                    z = e.getMessage();
                }
            }

            return z;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);

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