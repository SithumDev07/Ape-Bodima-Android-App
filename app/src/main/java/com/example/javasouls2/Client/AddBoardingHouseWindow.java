package com.example.javasouls2.Client;

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

import com.example.javasouls2.ClientCreatePt2;
import com.example.javasouls2.ClientCreatePt3;
import com.example.javasouls2.ClientLoginWindow;
import com.example.javasouls2.ClientPanel;
import com.example.javasouls2.CreateAccountAcitivity1;
import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.PreLoginAfterCreate;
import com.example.javasouls2.R;
import com.example.javasouls2.SearchMenu;
import com.example.javasouls2.SessionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import pl.droidsonroids.gif.GifTexImage2D;

public class AddBoardingHouseWindow extends AppCompatActivity implements DatabaseCredentials {

    //Variables
    private EditText addressLine1;
    private EditText addressLine2;
    private EditText addressLine3;
    private EditText city;
    private EditText postalCode;
    private EditText noOfRooms;
    private EditText Distance;

    private Button next;

    private ImageView back;
    private TextView Title;

    private String BoardingID;
    private String passingID;
    public static final String EXTRA_TEXT = "com.example.javasouls2.Client.AddBoardingHouseWindow.EXTRA_TEXT";

    private Statement statement;
    private Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_boarding_house_window);

        //Hooks
        addressLine1 = (EditText) findViewById(R.id.AddressLine1);
        addressLine2 = (EditText) findViewById(R.id.AddressLine2);
        addressLine3 = (EditText) findViewById(R.id.AddressLine3);
        city = (EditText) findViewById(R.id.AddressCity);
        postalCode = (EditText) findViewById(R.id.AddressPostalCode);
        noOfRooms = (EditText) findViewById(R.id.BoardingNoOfRooms);
        Distance = (EditText) findViewById(R.id.BoardingDistance);

        next = (Button) findViewById(R.id.NextBtn);

        back = (ImageView) findViewById(R.id.create_Acc_backButton);
        Title = (TextView) findViewById(R.id.CreateAccTextTitle);

        SessionManager sessionManager = new SessionManager(AddBoardingHouseWindow.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        GetBoardingID getBoardingID = new GetBoardingID();
        getBoardingID.execute("");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData updateData = new UpdateData();
                updateData.execute("");
            }
        });
    }

    public String getBoardingID(String ID){
            int lastDigits = Integer.parseInt(ID.substring(3,8));
            lastDigits++;

            if(lastDigits <= 9) ID = "BID0000" + Integer.toString(lastDigits);

            else if(lastDigits > 9 && lastDigits <= 99) ID = "BID000" + Integer.toString(lastDigits);

            else if(lastDigits > 99 && lastDigits <= 999) ID = "BID00" + Integer.toString(lastDigits);

            else if(lastDigits > 999 && lastDigits <= 9999) ID = "BID0" + Integer.toString(lastDigits);

            else ID = "BID" + Integer.toString(lastDigits);

        return ID;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), ClientPanel.class);
        startActivity(intent);

    }

    public class GetBoardingID extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {



            if (passingID.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT TOP 1 * FROM BoardingHouse ORDER BY BoardingID desc;";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";

                            BoardingID = resultSet.getString("BoardingID");
                            BoardingID = getBoardingID(BoardingID);


                            isSuccess = true;
                            connection.close();
                        } else {
                            BoardingID = "BID00000";
                            z = "Login Failed Invalid credentials";
                            isSuccess = false;
                        }

                        SessionManager sessionManager = new SessionManager(AddBoardingHouseWindow.this,SessionManager.SESSION_REMEMBERME_BOARDING_HOUSE);
                        sessionManager.createRememberMeSessionBoardingHouse(BoardingID);
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    z = e.getMessage();
                }

            }

        }

        @Override
        protected void onPostExecute(String r)
        {
        }

        @Override
        protected String doInBackground(String... params) {

            return z;
        }
    }



    public class UpdateData extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(AddBoardingHouseWindow.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(AddBoardingHouseWindow.this , "Just one more step" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddBoardingHouseRoomsWindow.class);

                //Animation

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(back,"animation_back_btn");
                pairs[1] = new Pair<View,String>(Title,"animation_next_btn");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddBoardingHouseWindow.this,pairs);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String Line1 = addressLine1.getText().toString();
            String Line2 = addressLine2.getText().toString();
            String Line3 = addressLine3.getText().toString();
            String City = city.getText().toString();
            String PostalCode = postalCode.getText().toString();
            String NoOfRooms = noOfRooms.getText().toString();
            String DistanceMeters = Distance.getText().toString();


            if(Line1.trim().equals("") || City.trim().equals("") || NoOfRooms.trim().equals("")){

                z = "Please enter valid username and password";

            }
            else{
                try {
                    connection = connectionclass(User,Pass,DB,IP,PORT);

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{

                        //SessionManager sessionManager = new SessionManager(AddBoardingHouseWindow.this,SessionManager.SESSION_REMEMBERME);
                        //sessionManager.createRememberMeSessionBoardingRooms(Integer.parseInt(NoOfRooms),1);

                        String query = "insert into BoardingHouse(BoardingID,NIC,AddressLine1,AddressLine2,AddressLine3,City,PostalCode,NoOfRooms,DistanceFromTheUniversity) values ('" + BoardingID + "','" + passingID + "','" + Line1 + "','" + Line2 + "','" + Line3 + "','" + City + "'," + PostalCode + "," + NoOfRooms + "," + DistanceMeters + ");";
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
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ":" + port + "/" + database;
            //con = DriverManager.getConnection(ConnectionURL,user,password);

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