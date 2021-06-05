package com.example.javasouls2.Client;

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
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javasouls2.AddPhotoBoardingHouseRooms;
import com.example.javasouls2.AddedBoardingSplash;
import com.example.javasouls2.ClientPanel;
import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.MainActivity;
import com.example.javasouls2.R;
import com.example.javasouls2.SessionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class AddBoardingHouseRoomsWindow extends AppCompatActivity implements DatabaseCredentials {

    private EditText roomNo;
    private EditText beds;
    private EditText months;
    private EditText keyMoney;
    private EditText rental;

    private int Rooms;
    private int newRooms;
    private int Current;

    private String OccupiedSelected = "NO";
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
    private Button addAnother;

    private Statement statement;
    private Connection connection;

    private String RoomID;

    private String BoardingID;
    public static final String EXTRA_TEXT = "com.example.javasouls2.Client.AddBoardingHouseRoomsWindow.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_boarding_house_rooms_window);

        roomNo = (EditText) findViewById(R.id.RoomNo);
        beds = (EditText) findViewById(R.id.BedsBR);
        months = (EditText) findViewById(R.id.monthsKeyMoney);
        keyMoney = (EditText) findViewById(R.id.keyMoneyAmount);
        rental = (EditText) findViewById(R.id.rentalPP);

        OccupiedYes = (RadioButton) findViewById(R.id.IsOccupiedYes);
        OccupiedNo = (RadioButton) findViewById(R.id.IsOccupiedNo);

        Male = (RadioButton) findViewById(R.id.MaleChecked);
        Female = (RadioButton) findViewById(R.id.FemaleChecked);

        IsElectricityChecked = (RadioButton) findViewById(R.id.ElectricityYes);
        IsElectricityCheckedNo = (RadioButton) findViewById(R.id.ElectricityNo);

        waterBillIncluded = (RadioButton) findViewById(R.id.WaterBillYes);
        waterBillIncludedNo = (RadioButton) findViewById(R.id.WaterBillNo);

        attachedBathroomYes = (RadioButton) findViewById(R.id.AttachbathroomYes);
        attachedBathroomNo = (RadioButton) findViewById(R.id.AttachedbathroomNo);

        kitchenChecked = (RadioButton) findViewById(R.id.kitchenYes);
        kitchenCheckedNo = (RadioButton) findViewById(R.id.kitchenNo);

        next = (Button) findViewById(R.id.NextBtnBoardingRoom);

        SessionManager sessionManager = new SessionManager(AddBoardingHouseRoomsWindow.this,SessionManager.SESSION_REMEMBERME_BOARDING_HOUSE);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsBoardingHouseFromSession();
        BoardingID = rememberMeDetails.get(SessionManager.KEY_SESSION_BOARDING_HOUSEID);

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

        GetRoomID getRoomID = new GetRoomID();
        getRoomID.execute("");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData updateData = new UpdateData();
                updateData.execute("");
            }
        });
    }


    public String getRoomID(String ID){
        int lastDigits = Integer.parseInt(ID.substring(3,8));
        lastDigits++;

        if(lastDigits <= 9) ID = "RID0000" + Integer.toString(lastDigits);

        else if(lastDigits > 9 && lastDigits <= 99) ID = "RID000" + Integer.toString(lastDigits);

        else if(lastDigits > 99 && lastDigits <= 999) ID = "RID00" + Integer.toString(lastDigits);

        else if(lastDigits > 999 && lastDigits <= 9999) ID = "RID0" + Integer.toString(lastDigits);

        else ID = "RID" + Integer.toString(lastDigits);

        return ID;
    }

    public class GetRoomID extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            if (BoardingID.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT TOP 1 * FROM BoardingHouseRooms ORDER BY RoomID desc;";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";

                            RoomID = resultSet.getString("RoomID");
                            RoomID = getRoomID(RoomID);
                            //BID00000

                            isSuccess = true;
                            connection.close();
                        } else {
                            RoomID = "RID00000";
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
            Toast.makeText(AddBoardingHouseRoomsWindow.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(AddBoardingHouseRoomsWindow.this , "Just one more step" , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), AddPhotoBoardingHouseRooms.class);

                intent.putExtra(EXTRA_TEXT,RoomID);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String RoomNo = roomNo.getText().toString();
            String NoOfBeds = beds.getText().toString();
            String Months = months.getText().toString();
            String Amount = keyMoney.getText().toString();
            String rentalPP = rental.getText().toString();


            if(BoardingID.trim().equals("") || RoomID.trim().equals("")){

                z = "Please enter valid username and password";

            }
            else{
                try {
                    connection = connectionclass(User,Pass,DB,IP,PORT);

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "INSERT INTO BoardingHouseRooms(RoomID,BoardingID,RoomNo,IsOccupied,NoOfBeds,MaleOrFemale,WaterBill,KeyMoneyMonths,KeyMoney,RentalPerPerson,AttachedBathroom,Kitchen,ElectricityBill) VALUES ('" + RoomID + "','" + BoardingID + "','" + RoomNo +  "','"  + OccupiedSelected +  "'," + NoOfBeds + ",'" + MaleFemaleSelected + "','" + waterBillSelected + "'," + Months +"," + Amount +  "," + rentalPP + ",'" + AttachedBathroomSelected + "','" + KitchenSelected + "','" + electricity + "');";
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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), ClientPanel.class);
        startActivity(intent);

    }

    public void BackBtn(View view){

        Intent intent = new Intent(getApplicationContext(), ClientPanel.class);
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
            //ConnectionURL = "jdbc:jtds:sqlserver://" + server + ":" + port + "/" + database;
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