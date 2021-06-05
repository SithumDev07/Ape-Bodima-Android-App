package com.example.javasouls2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javasouls2.Client.AdEditWindowClient;
import com.example.javasouls2.Client.AllAnnexes;
import com.example.javasouls2.Client.AllBoardings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class AdEditWindowClinetBoarding extends AppCompatActivity implements DatabaseCredentials {

    private EditText beds;
    private EditText months;
    private EditText keyMoney;
    private EditText rental;

    private String OccupiedSelected = "No";
    private RadioButton OccupiedYes;
    private RadioButton OccupiedNo;

    private String MaleFemaleSelected = "Male";
    private RadioButton Male;
    private RadioButton Female;

    private String electricity = "Yes";
    private RadioButton IsElectricityChecked;
    private RadioButton IsElectricityCheckedNo;

    private String waterBillSelected = "Yes";
    private RadioButton waterBillIncluded;
    private RadioButton waterBillIncludedNo;

    private String AttachedBathroomSelected = "Yes";
    private RadioButton attachedBathroomYes;
    private RadioButton attachedBathroomNo;

    private String KitchenSelected = "No";
    private RadioButton kitchenChecked;
    private RadioButton kitchenCheckedNo;

    private Button next;
    private Button Delete;

    private Statement statement;
    private Connection connection;

    private String RoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ad_edit_window_clinet_boarding);

        beds = (EditText) findViewById(R.id.BedsBRBoarding);
        months = (EditText) findViewById(R.id.monthsKeyMoneyBoarding);
        keyMoney = (EditText) findViewById(R.id.keyMoneyAmountBoarding);
        rental = (EditText) findViewById(R.id.rentalPPBoarding);

        OccupiedYes = (RadioButton) findViewById(R.id.IsOccupiedYesBoarding);
        OccupiedNo = (RadioButton) findViewById(R.id.IsOccupiedNoBoarding);

        Male = (RadioButton) findViewById(R.id.MaleCheckedBoarding);
        Female = (RadioButton) findViewById(R.id.FemaleCheckedBoarding);

        IsElectricityChecked = (RadioButton) findViewById(R.id.ElectricityYesBoarding);
        IsElectricityCheckedNo = (RadioButton) findViewById(R.id.ElectricityNoBoarding);

        waterBillIncluded = (RadioButton) findViewById(R.id.WaterBillYesBoarding);
        waterBillIncludedNo = (RadioButton) findViewById(R.id.WaterBillNoBoarding);

        attachedBathroomYes = (RadioButton) findViewById(R.id.AttachbathroomYesBoarding);
        attachedBathroomNo = (RadioButton) findViewById(R.id.AttachedbathroomNoBoarding);

        kitchenChecked = (RadioButton) findViewById(R.id.kitchenYesBoarding);
        kitchenCheckedNo = (RadioButton) findViewById(R.id.kitchenNoBoarding);

        next = (Button) findViewById(R.id.NextBtnBoardingRoomBoarding);
        Delete = (Button) findViewById(R.id.DeleteBtnEditBoarding);

        SessionManager sessionManagerBoarding = new SessionManager(AdEditWindowClinetBoarding.this, SessionManager.SESSION_REMEMBERME_BOARDING);
        HashMap<String, String> rememberMeDetailsBoarding = sessionManagerBoarding.getRememberMeDetailsBoardingFromSession();
        RoomID = rememberMeDetailsBoarding.get(SessionManager.KEY_SESSIONBOARDINGID);

        SetPreData setPreData = new SetPreData();
        setPreData.execute("");

        //Radio Button Section

        OccupiedYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OccupiedSelected = "Yes";
            }
        });

        OccupiedNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OccupiedSelected = "No";
            }
        });

        Male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MaleFemaleSelected = "Male";
            }
        });

        Female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MaleFemaleSelected = "Female";
            }
        });

        IsElectricityChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                electricity = "Yes";
            }
        });

        IsElectricityCheckedNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                electricity = "No";
            }
        });

        waterBillIncluded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                waterBillSelected = "Yes";
            }
        });

        waterBillIncludedNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                waterBillSelected = "No";
            }
        });

        attachedBathroomYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AttachedBathroomSelected = "Yes";
            }
        });

        attachedBathroomNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AttachedBathroomSelected = "No";
            }
        });

        kitchenChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KitchenSelected = "Yes";
            }
        });

        kitchenCheckedNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KitchenSelected = "No";
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

                AlertDialog dialog = new AlertDialog.Builder(AdEditWindowClinetBoarding.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure want to delete boarding ad?")
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

                                String queryPhotos = "delete from BoardingHouseRoomsPhoto where RoomID = '" + RoomID + "';";
                                Statement statement = connection.createStatement();
                                statement.executeUpdate(queryPhotos);

                                String queryDeleteRating = "delete from BoardingRoomRating where RoomID = '" + RoomID + "';";
                                statement.executeUpdate(queryDeleteRating);

                                String queryDeleteAlreadyUser = "delete from UserBoardingHouseRooms where RoomID = '" + RoomID + "';";
                                statement.executeUpdate(queryDeleteAlreadyUser);

                                String queryDeleteNonUser = "delete from NonUserOccupierBoarding where RoomID = '" + RoomID + "';";
                                statement.executeUpdate(queryDeleteNonUser);

                                String queryDeleteAd = "delete from BoardingHouseRooms where RoomID = '" + RoomID + "';";
                                statement.executeUpdate(queryDeleteAd);
                            }

                            connection.close();
                        } catch (Exception e) {

                        }

                        Toast.makeText(AdEditWindowClinetBoarding.this, "Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), AllBoardings.class);

                        startActivity(intent);
                    }
                });

            }
        });

    }

    public class SetPreData extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            if (RoomID.trim().equals("")) {

                z = "Passing ID Failure";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT * FROM BoardingHouseRooms WHERE RoomID = '" + RoomID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Loading Data";


                            beds.setText(resultSet.getString("NoOfBeds"));
                            months.setText(resultSet.getString("KeyMoneyMonths"));
                            keyMoney.setText(resultSet.getString("KeyMoney"));
                            rental.setText(resultSet.getString("RentalPerPerson"));

                            /*String Electricity = resultSet.getString("ElectricityBill");
                            if(Electricity.equals("Yes")){
                                IsElectricityChecked.setChecked(true);
                            }
                            else{
                                IsElectricityCheckedNo.setChecked(true);
                            }

                            String Occupy = resultSet.getString("IsOccupied");
                            if(Occupy.equals("Yes")){
                                OccupiedYes.setChecked(true);
                            }
                            else{
                                OccupiedNo.setChecked(true);
                            }

                            String WaterBill = resultSet.getString("WaterBill");
                            if(WaterBill.equals("Yes")){
                                waterBillIncluded.setChecked(true);
                            }
                            else{
                                waterBillIncludedNo.setChecked(true);
                            }

                            String AttachedBathrooms = resultSet.getString("AttachedBathroom");
                            if(AttachedBathrooms.equals("Yes")){
                                attachedBathroomYes.setChecked(true);
                            }
                            else{
                                attachedBathroomNo.setChecked(true);
                            }

                            String Kitchen = resultSet.getString("Kitchen");
                            if(Kitchen.equals("Yes")){
                                kitchenChecked.setChecked(true);
                            }
                            else{
                                kitchenCheckedNo.setChecked(true);
                            }

                            String MaleOrFemale = resultSet.getString("MaleOrFemale");
                            if (MaleOrFemale.equals("Male")){

                                Male.setChecked(true);

                            }
                            else{
                                Female.setChecked(true);
                            }*/

                            isSuccess = true;
                            connection.close();
                        } else {
                            z = "Fatal Error - Loading Error! Invalid RoomID";
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
            Toast.makeText(AdEditWindowClinetBoarding.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(AdEditWindowClinetBoarding.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AllBoardings.class);

                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String Beds = beds.getText().toString();
            String Months = months.getText().toString();
            String KeyMoney = keyMoney.getText().toString();
            String Rental = rental.getText().toString();


            if (RoomID.trim().equals("")) {

                z = "FATAL : Passing ID Failure!!!";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "update BoardingHouseRooms set IsOccupied = '" + OccupiedSelected + "',NoOfBeds = '" + Beds + "',MaleOrFemale = '" + MaleFemaleSelected + "',WaterBill = '" + waterBillSelected + "',KeyMoneyMonths = '" + Months + "',KeyMoney = '" + KeyMoney + "',RentalPerPerson = '" + Rental + "',AttachedBathroom = '" + AttachedBathroomSelected + "',Kitchen = '" + KitchenSelected + "',ElectricityBill = '" + electricity + "' where RoomID = '" + RoomID + "';";

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

    public void BackBtn(View view) {

        Intent intent = new Intent(getApplicationContext(),AllBoardings.class);

        startActivity(intent);


    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server, String port) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");


            //ConnectionURL = "jdbc:jtds:sqlserver://apebodima.database.windows.net:1433;DatabaseName=ApeBodima;user=adminApeBodima@apebodima;password=Fr13nds2552;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";


            con = DriverManager.getConnection(ConnectionURLS, UserD, PassD);

        } catch (SQLException se) {
            Log.e("Error here 1: ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Error here 2: ", e.getMessage());
        } catch (Exception e) {
            Log.e("Error here 3: ", e.getMessage());
        }
        return con;
    }
}