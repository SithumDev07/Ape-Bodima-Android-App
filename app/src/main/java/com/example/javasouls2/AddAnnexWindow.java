package com.example.javasouls2;

import android.annotation.SuppressLint;
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

import com.example.javasouls2.Client.SplashScreenAfterAnnex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class AddAnnexWindow extends AppCompatActivity implements DatabaseCredentials {

    private EditText AddressLine1;
    private EditText AddressLine2;
    private EditText AddressLine3;
    private EditText City;
    private EditText PostalCode;
    private EditText NoOfRooms;
    private EditText KeyMoneyMonths;
    private EditText KeyMoney;
    private EditText RentalPerMonth;
    private EditText NoOfBathrooms;
    private EditText Distance;

    private String selectedValue = "No";
    private RadioButton OccupiedYes;
    private RadioButton OccupiedNo;

    private String IsFurnishedDefault = "No";
    private RadioButton FurnishedYes;
    private RadioButton FurnishedNo;

    private Button next;

    private Connection connection;
    private Statement statement;

    private String AnnexID;
    private String passingID;

    public static final String EXTRA_TEXT = "com.example.javasouls2.AddAnnexWindow.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_annex_window);

        //Hooks
        AddressLine1 = (EditText) findViewById(R.id.AddressLine1);
        AddressLine2 = (EditText) findViewById(R.id.AddressLine2);
        AddressLine3 = (EditText) findViewById(R.id.AddressLine3);
        City = (EditText) findViewById(R.id.AddressCity);
        PostalCode = (EditText) findViewById(R.id.AddressPostalCode);
        NoOfRooms = (EditText) findViewById(R.id.AnnexNoOfRooms);
        KeyMoneyMonths = (EditText) findViewById(R.id.KeyMoneyMonths);
        KeyMoney = (EditText) findViewById(R.id.KeyMoney);
        RentalPerMonth = (EditText) findViewById(R.id.RentalPerMonth);
        NoOfBathrooms = (EditText) findViewById(R.id.NoOfBathrooms);
        Distance = (EditText) findViewById(R.id.AnnexDistance);

        OccupiedYes = (RadioButton) findViewById(R.id.IsOccupiedYes);
        OccupiedNo = (RadioButton) findViewById(R.id.IsOccupiedNo);

        FurnishedYes = (RadioButton) findViewById(R.id.IsFurnishedYes);
        FurnishedNo = (RadioButton) findViewById(R.id.IsFurnishedNo);

        next = (Button) findViewById(R.id.NextBtn);

        SessionManager sessionManager = new SessionManager(AddAnnexWindow.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);



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

        GetAnnexID getAnnexID = new GetAnnexID();
        getAnnexID.execute("");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData updateData = new UpdateData();
                updateData.execute("");
            }
        });
    }

    public String getAnnexID(String ID) {
        int lastDigits = Integer.parseInt(ID.substring(3, 8));
        lastDigits++;

        if (lastDigits <= 9) ID = "AID0000" + Integer.toString(lastDigits);

        else if (lastDigits > 9 && lastDigits <= 99) ID = "AID000" + Integer.toString(lastDigits);

        else if (lastDigits > 99 && lastDigits <= 999) ID = "AID00" + Integer.toString(lastDigits);

        else if (lastDigits > 999 && lastDigits <= 9999) ID = "AID0" + Integer.toString(lastDigits);

        else ID = "AID" + Integer.toString(lastDigits);

        return ID;
    }

    public class GetAnnexID extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {


            if (passingID.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT TOP 1 * FROM Annex ORDER BY AnnexID desc;";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successfull";

                            AnnexID = resultSet.getString("AnnexID");
                            AnnexID = getAnnexID(AnnexID);
                            //AID00000

                            isSuccess = true;
                            connection.close();
                        } else {
                            AnnexID = "AID00000";
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

    public class UpdateData extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(AddAnnexWindow.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(AddAnnexWindow.this, "Just one more step", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddPhotoAnnex.class);

                intent.putExtra(EXTRA_TEXT,AnnexID);
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


            if (passingID.trim().equals("") || selectedValue.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "INSERT INTO Annex(AnnexID,NIC,IsOccupied,AddressLine1,AddressLine2,AddressLine3,City,PostalCode,NoOfRooms,KeyMoneyMonths,KeyMoney,RentalPerMonth,NoOfBathrooms,DistanceFromTheUniversity,IsFurnished) VALUES ('" + AnnexID + "','" + passingID + "','" + selectedValue + "','" + Line1 + "','" + Line2 + "','" + Line3 + "','" + city + "'," + postalCode + "," + noOfRooms + "," + keyMoneyMonths + "," + keyMoneyAmount + "," + rental + "," + noOfBathrooms + "," + DistanceMeters + ",'" + IsFurnishedDefault + "');";
                        statement = connection.createStatement();
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