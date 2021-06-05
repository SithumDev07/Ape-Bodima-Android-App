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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class UserCreateAcc3 extends AppCompatActivity implements DatabaseCredentials{

    private ImageView mainLogo;
    private TextView Logo;
    private TextView slogan;
    private Button finish;
    private Button logBack;

    private EditText Addressline1;
    private EditText Addressline2;
    private EditText CityAddress;
    private EditText PostalAddress;

    private Connection connection;
    private String passingID;
    private Statement statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_create_acc3);

        mainLogo = (ImageView) findViewById(R.id.LogoImageUser);
        Logo = (TextView) findViewById(R.id.LogoNameUserCreate);
        slogan = (TextView) findViewById(R.id.slogan_name_user);
        finish = (Button) findViewById(R.id.FinishBtnUser);
        logBack = (Button) findViewById(R.id.LogBackBtnUser);

        Addressline1 = (EditText) findViewById(R.id.AddressLine1InputUser);
        Addressline2 = (EditText) findViewById(R.id.AddressLine2InputUser);
        CityAddress = (EditText) findViewById(R.id.CityAddressInputUser);
        PostalAddress = (EditText) findViewById(R.id.PostalCodeInputUser);

        SessionManager sessionManager = new SessionManager(UserCreateAcc3.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

    }

    public void Update(View view){
        UpdateUser updateUser = new UpdateUser();
        updateUser.execute("");
    }

    public void moveLogBack(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        Pair[] pairs = new Pair[7];

        pairs[0] = new Pair<View, String>(mainLogo, "logo_image");
        pairs[1] = new Pair<View, String>(Logo, "logo_text");
        pairs[2] = new Pair<View, String>(slogan, "slogan_text");
        pairs[3] = new Pair<View, String>(Addressline1, "username_");
        pairs[4] = new Pair<View, String>(CityAddress, "password_");
        pairs[5] = new Pair<View, String>(finish, "Next");
        pairs[6] = new Pair<View, String>(logBack, "logback");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserCreateAcc3.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public class UpdateUser extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(UserCreateAcc3.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(UserCreateAcc3.this , "One More step!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserPhoto.class);

                startActivity(intent);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String Line1 = Addressline1.getText().toString();
            String Line2 = Addressline2.getText().toString();
            String City = CityAddress.getText().toString();
            String Postal = PostalAddress.getText().toString();


            if(passingID.trim().equals("")){

                z = "Fatal Error";

            }
            else{
                try {
                    connection = connectionclass(User,Pass,DB,IP,PORT);

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "UPDATE AppUser SET AddressLine1 = '" + Line1 + "',AddressLine2 = '" + Line2 + "',City = '" + City + "',PostalCode = " + Postal + " WHERE Username = '" + passingID + "';";
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