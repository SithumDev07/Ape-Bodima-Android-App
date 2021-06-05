package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.javasouls2.Client.ClientEditAccountWindow;
import com.example.javasouls2.Client.DataValidationUserCheck;
import com.example.javasouls2.User.DataValidationUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ClientLoginWindow extends AppCompatActivity implements ClientCommandInterface, DatabaseCredentials {

    private CheckBox rememberME;
    private Button createAnAcc;
    private Button Login;
    private ImageView backBtn;

    private EditText username;
    private EditText password;
    private Connection connection;

    public static final String EXTRA_TEXT = "com.example.javasouls2.ClientLoginWindow.EXTRA_TEXT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_login_window);

        createAnAcc = (Button) findViewById(R.id.createAnAccountBtn);
        backBtn = (ImageView) findViewById(R.id.login_backButton);

        Login = (Button) findViewById(R.id.ClientLoginBtn);

        username = (EditText) findViewById(R.id.clientUsername);
        password = (EditText) findViewById(R.id.clientPassword);
        rememberME = (CheckBox) findViewById(R.id.RememberMe);

        SessionManager sessionManager = new SessionManager(ClientLoginWindow.this,SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberme()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            username.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONNIC));
            password.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("rememberME","");

        if(checkbox.equals("true")){
            Intent passintent = new Intent(getApplicationContext(), SearchMenu.class);
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            String passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);
            passintent.putExtra(EXTRA_TEXT,passingID);
            startActivity(passintent);
            finish();
        }
        else if(checkbox.equals("false")){
            Toast.makeText(ClientLoginWindow.this,"Please Login",Toast.LENGTH_SHORT).show();
        }

        rememberME.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rememberME","true");
                    editor.apply();
                    Toast.makeText(ClientLoginWindow.this,"Checked",Toast.LENGTH_SHORT).show();
                }
                else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rememberME","false");
                    editor.apply();
                    Toast.makeText(ClientLoginWindow.this,"Unchecked",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void LoginOwner(View view) throws SQLException {

        DataValidationUserCheck dataValidationUserCheck = new DataValidationUserCheck(username,password);

        if(dataValidationUserCheck.GetValidationUsernameOwner() | dataValidationUserCheck.GetValidationPasswordOwner()){

            CheckLogin checkLogin = new CheckLogin();
            checkLogin.execute("");

        }
        else {

            return;
        }

    }

    public void MoveTomain(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public void callClientCreateAccount1(View view) {
        Intent intent = new Intent(getApplicationContext(), ClientCreateAccount.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(findViewById(R.id.createAnAccountBtn), "transition_create");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientLoginWindow.this, pairs);

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
            Toast.makeText(ClientLoginWindow.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(ClientLoginWindow.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SearchMenu.class);
                String passingID = username.getText().toString();
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String user = username.getText().toString();
            String pass = password.getText().toString();

            SessionManager sessionManager = new SessionManager(ClientLoginWindow.this,SessionManager.SESSION_REMEMBERME);
            sessionManager.createRememberMenSession(user,pass);

            if (user.trim().equals("") || pass.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT * FROM Client WHERE NIC= '" + user.toString() + "' AND Password1= '" + pass.toString() + "';";
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

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server, String port){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");


           // ConnectionURL = "jdbc:jtds:sqlserver://apebodima.database.windows.net:1433;DatabaseName=ApeBodima;user=adminApeBodima@apebodima;password=Fr13nds2552;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";


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