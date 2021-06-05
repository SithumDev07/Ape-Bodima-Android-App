package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.example.javasouls2.Client.DataValidationOccupyUserAnnex;
import com.example.javasouls2.Client.DataValidationUserCheck;
import com.example.javasouls2.User.UserLoginSplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements DatabaseCredentials{


    //Variables
    private ImageView logoMain;
    private TextView Logo;
    private TextView slogan;

    private EditText username;
    private EditText password;

    private CheckBox rememberME;

    private Button login;
    private Button createAcc;

    private Connection connection;

    public static final String EXTRA_TEXT = "com.example.javasouls2.LoginActivity.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);



        //Hooks
        logoMain = (ImageView) findViewById(R.id.LogoImage);
        Logo = (TextView) findViewById(R.id.LogoName);
        slogan = (TextView) findViewById(R.id.slogan_name);

        username = (EditText) findViewById(R.id.UserInput);
        password = (EditText) findViewById(R.id.PasswordInput);

        rememberME = (CheckBox) findViewById(R.id.RememberMe);

        login = (Button) findViewById(R.id.GoBtn);
        createAcc = (Button) findViewById(R.id.CreateAccBtn);

        SessionManager sessionManager = new SessionManager(LoginActivity.this,SessionManager.SESSION_REMEMBERMEUSER);
        if(sessionManager.checkRemembermeUser()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
            username.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONUSER));
            password.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONUSERPASSWORD));

            //SetPreProfilePicture setPreProfilePicture = new SetPreProfilePicture();
            //setPreProfilePicture.execute("");
        }

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("rememberME","");

        if(checkbox.equals("true")){
            Intent passintent = new Intent(getApplicationContext(), UserDashboard.class);
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
            String passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);
            passintent.putExtra(EXTRA_TEXT,passingID);
            startActivity(passintent);
            finish();
        }
        else if(checkbox.equals("false")){
            Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
        }

        rememberME.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rememberME","true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Checked",Toast.LENGTH_SHORT).show();
                }
                else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rememberME","false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Unchecked",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void Login(View view) throws SQLException {

        DataValidationUserCheck dataValidationUserCheck = new DataValidationUserCheck(username,password);

        if(dataValidationUserCheck.GetValidationUsername() | dataValidationUserCheck.GetValidationPassword()){
            CheckLogin checkLogin = new CheckLogin();
            checkLogin.execute("");
        }
        else {

            return;
        }

    }

    public void moveUserCreate(View view){
        Intent intent = new Intent(getApplicationContext(), CreateAccountAcitivity1.class);

        Pair[] pairs = new Pair[7];

        pairs[0] = new Pair<View, String>(logoMain, "logo_image");
        pairs[1] = new Pair<View, String>(Logo, "logo_text");
        pairs[2] = new Pair<View, String>(slogan, "slogan_text");
        pairs[3] = new Pair<View, String>(username, "username_");
        pairs[4] = new Pair<View, String>(password, "password_");
        pairs[5] = new Pair<View, String>(login, "Next");
        pairs[6] = new Pair<View, String>(createAcc, "logback");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public class CheckLogin extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);

                Pair[] pairs = new Pair[1];

                //Since we don't need any use of Client Area Button
                pairs[0] = new Pair<View, String>(login, "logo_image");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

                String passingID = username.getText().toString();
                startActivity(intent,options.toBundle());
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String user = username.getText().toString();
            String pass = password.getText().toString();


            SessionManager sessionManager = new SessionManager(LoginActivity.this,SessionManager.SESSION_REMEMBERMEUSER);
            sessionManager.createRememberMeSessionUser(user,pass);

            if (user.trim().equals("") || pass.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT * FROM AppUser WHERE Username = '" + user + "' AND UserPassword = '" + pass + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";
                            isSuccess = true;
                            connection.close();
                        } else {
                            z = "Login Failed Invalid credentials";
                            isSuccess = false;
                        }
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    z = e.getMessage();
                }


            }

            return z;
        }


    }

    /*public class SetPreProfilePicture extends AsyncTask<String, String, String> {

        String z = "";
        String Username = username.getText().toString();
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            if (Username.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT * FROM AppUser WHERE Username = '" + Username + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Here You Back!";

                            String fetchProfilePicture = "SELECT ProfilePicture from AppUser where UserName = '" + Username + "';";
                            ResultSet resultSetImage = statement.executeQuery(fetchProfilePicture);
                            resultSetImage.next();

                            byte[] byteImageDb = resultSetImage.getBytes("ProfilePicture");

                            Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                            logoMain.setImageBitmap(bitmapImageDB);

                            isSuccess = true;
                            connection.close();
                        } else {
                            z = "Login Failed Invalid credentials";
                            isSuccess = false;
                        }
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    z = e.getMessage();
                }

            }

        }

        @Override
        protected void onPostExecute(String r) {

        }

        @Override
        protected String doInBackground(String... params) {

            return z;
        }
    }*/

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server, String port){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Connection con = null;
       // String ConnectionURL = null;

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