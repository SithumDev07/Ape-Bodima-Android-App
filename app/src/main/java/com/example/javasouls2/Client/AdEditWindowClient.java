package com.example.javasouls2.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.javasouls2.AdViewUser;
import com.example.javasouls2.AddAnnexWindow;
import com.example.javasouls2.AddPhotoAnnex;
import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.R;
import com.example.javasouls2.SearchMenu;
import com.example.javasouls2.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class AdEditWindowClient extends AppCompatActivity implements DatabaseCredentials {

    private TextInputEditText AddressLine1;
    private TextInputEditText AddressLine2;
    private TextInputEditText AddressLine3;
    private TextInputEditText City;
    private TextInputEditText PostalCode;
    private TextInputEditText NoOfRooms;
    private TextInputEditText KeyMoneyMonths;
    private TextInputEditText KeyMoney;
    private TextInputEditText RentalPerMonth;
    private TextInputEditText NoOfBathrooms;
    private TextInputEditText Distance;

    private String selectedValue = "No";
    private RadioButton OccupiedYes;
    private RadioButton OccupiedNo;

    private String IsFurnishedDefault = "No";
    private RadioButton FurnishedYes;
    private RadioButton FurnishedNo;

    private Button next;
    private Button Delete;

    private Connection connection;
    private Statement statement;

    private String AnnexID;
    private String passingID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ad_edit_window_client);

        //Hooks
        AddressLine1 = (TextInputEditText) findViewById(R.id.AddressLine1Edit);
        AddressLine2 = (TextInputEditText) findViewById(R.id.AddressLine2Edit);
        AddressLine3 = (TextInputEditText) findViewById(R.id.AddressLine3Edit);
        City = (TextInputEditText) findViewById(R.id.AddressCityEdit);
        PostalCode = (TextInputEditText) findViewById(R.id.AddressPostalCodeEdit);
        NoOfRooms = (TextInputEditText) findViewById(R.id.AnnexNoOfRoomsEdit);
        KeyMoneyMonths = (TextInputEditText) findViewById(R.id.KeyMoneyMonthsEdit);
        KeyMoney = (TextInputEditText) findViewById(R.id.KeyMoneyEdit);
        RentalPerMonth = (TextInputEditText) findViewById(R.id.RentalPerMonthEdit);
        NoOfBathrooms = (TextInputEditText) findViewById(R.id.NoOfBathroomsEdit);
        Distance = (TextInputEditText) findViewById(R.id.AnnexDistanceEdit);

        OccupiedYes = (RadioButton) findViewById(R.id.IsOccupiedYesEdit);
        OccupiedNo = (RadioButton) findViewById(R.id.IsOccupiedNoEdit);

        FurnishedYes = (RadioButton) findViewById(R.id.IsFurnishedYesEdit);
        FurnishedNo = (RadioButton) findViewById(R.id.IsFurnishedNoEdit);

        next = (Button) findViewById(R.id.NextBtnEdit);
        Delete = (Button) findViewById(R.id.DeleteBtnEdit);

        /*SessionManager sessionManager = new SessionManager(AdEditWindowClient.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);*/

        SessionManager sessionManager = new SessionManager(AdEditWindowClient.this, SessionManager.SESSION_REMEMBERME_ANNEX);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsAnnexFromSession();
        AnnexID = rememberMeDetails.get(SessionManager.KEY_SESSION_ANNEXID);

        SetPreData setPreData = new SetPreData();
        setPreData.execute("");

        OccupiedYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValue = "Yes";
            }
        });

        OccupiedNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValue = "No";
            }
        });

        FurnishedYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsFurnishedDefault = "Yes";
            }
        });

        FurnishedNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsFurnishedDefault = "No";
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAd updateAd = new UpdateAd();
                updateAd.execute("");
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(AdEditWindowClient.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure want to delete annex ad?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No",null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            connection = connectionclass(User, Pass, DB, IP, PORT);

                            if (connection == null) {

                            } else {

                                String queryPhotos = "delete from AnnexPhoto where AnnexID = '" + AnnexID + "';";
                                Statement statement = connection.createStatement();
                                statement.executeUpdate(queryPhotos);

                                String queryDeleteRating = "delete from AnnexRating where AnnexID = '" + AnnexID + "';";
                                statement.executeUpdate(queryDeleteRating);

                                String queryDeleteAlreadyUser = "delete from UserAnnex where AnnexID = '" + AnnexID + "';";
                                statement.executeUpdate(queryDeleteAlreadyUser);

                                String queryDeleteNonUser = "delete from NonUserOccupierAnnex where AnnexID = '" + AnnexID + "';";
                                statement.executeUpdate(queryDeleteNonUser);

                                String queryDeleteAd = "delete from Annex where AnnexID = '" + AnnexID + "';";
                                statement.executeUpdate(queryDeleteAd);
                            }

                            connection.close();
                        } catch (Exception e) {

                        }

                        Toast.makeText(AdEditWindowClient.this, "Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), AllAnnexes.class);

                        startActivity(intent);
                    }
                });

            }
        });


    }

    /*public void DeleteAdAnnex(View view){

        try {
            connection = connectionclass(User, Pass, DB, IP, PORT);

            if (connection == null) {

            } else {

                String queryPhotos = "delete from AnnexPhoto where AnnexID = '" + AnnexID + "';";
                Statement statement = connection.createStatement();
                statement.executeUpdate(queryPhotos);

                String queryDeleteAd = "delete from Annex where AnnexID = '" + AnnexID + "';";
                statement.executeUpdate(queryDeleteAd);

                String queryDeleteRating = "delete from AnnexRating where AnnexID = '" + AnnexID + "';";
                statement.executeUpdate(queryDeleteRating);

                String queryDeleteAlreadyUser = "delete from UserAnnex where AnnexID = '" + AnnexID + "';";
                statement.executeUpdate(queryDeleteAlreadyUser);

                String queryDeleteNonUser = "delete from NonUserOccupierAnnex where AnnexID = '" + AnnexID + "';";
                statement.executeUpdate(queryDeleteNonUser);
            }

            connection.close();
        } catch (Exception e) {

        }

        Toast.makeText(AdEditWindowClient.this, "Deleted", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), AllAnnexes.class);

        startActivity(intent);

    }*/

    public class SetPreData extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            if (AnnexID.trim().equals("")) {

                z = "Passing ID Failure";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT * FROM Annex WHERE AnnexID = '" + AnnexID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Loading Data";


                            AddressLine1.setText(resultSet.getString("AddressLine1"));
                            AddressLine2.setText(resultSet.getString("AddressLine2"));
                            AddressLine3.setText(resultSet.getString("AddressLine3"));
                            City.setText(resultSet.getString("City"));
                            PostalCode.setText(resultSet.getString("PostalCode"));
                            NoOfRooms.setText(resultSet.getString("NoOfRooms"));
                            KeyMoneyMonths.setText(resultSet.getString("KeyMoneyMonths"));
                            KeyMoney.setText(resultSet.getString("KeyMoney"));
                            RentalPerMonth.setText(resultSet.getString("RentalPerMonth"));
                            NoOfBathrooms.setText(resultSet.getString("NoOfBathrooms"));
                            Distance.setText(resultSet.getString("DistanceFromTheUniversity"));


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
    }

    public class UpdateAd extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(AdEditWindowClient.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(AdEditWindowClient.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AllAnnexes.class);

                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String Line1 = AddressLine1.getText().toString();
            String Line2 = AddressLine2.getText().toString();
            String Line3 = AddressLine3.getText().toString();
            String city = City.getText().toString();
            String postalCode = PostalCode.getText().toString();
            String noOfRooms = NoOfRooms.getText().toString();
            String keyMoneyMonths = KeyMoneyMonths.getText().toString();
            String keyMoneyAmount = KeyMoney.getText().toString();
            String rental = RentalPerMonth.getText().toString();
            String noOfBathrooms = NoOfBathrooms.getText().toString();
            String DistanceMeters = Distance.getText().toString();

            if (AnnexID.trim().equals("")) {

                z = "FATAL : Passing ID Failure!!!";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "update Annex set AddressLine1 = '" + Line1 + "',AddressLine2 = '" + Line2 + "',AddressLine3 = '" + Line3 + "',City = '" + city + "',PostalCode = " + postalCode + ",NoOfRooms = " + noOfRooms + ",KeyMoneyMonths = " + keyMoneyMonths + ",KeyMoney = " + keyMoneyAmount + ",RentalPerMonth = " + rental + ",NoOfBathrooms = " + noOfBathrooms + ",DistanceFromTheUniversity = " + DistanceMeters + ",IsOccupied = '" +selectedValue + "',IsFurnished = '" + IsFurnishedDefault + "' where AnnexID = '" + AnnexID + "';";

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