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

import com.example.javasouls2.User.FilterAndSearchMenuUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class FilterMenuBoardings extends AppCompatActivity implements DatabaseCredentials{

    private Connection connection;

    private TextView TitleTop;
    private ImageView DrawerLeft;
    private ImageView AccountIcon;

    private String selectedValueGender = "";
    private RadioButton ForGirls;
    private RadioButton ForBoys;

    private String selectedValueElectricity = "";
    private RadioButton ElectricityYes;
    private RadioButton ElectricityNo;

    private String selectedValueWaterBill = "";
    private RadioButton WaterBillYes;
    private RadioButton WaterBilNo;

    private String selectedValueBathrooms = "";
    private RadioButton BathroomsYes;
    private RadioButton BathroomsNo;

    private String selectedValueKitchen = "";
    private RadioButton KitchenYes;
    private RadioButton KitchenNo;

    private EditText StartRental;
    private EditText EndRental;
    private EditText Beds;
    private EditText StartKeyMoney;
    private EditText EndKeyMoney;
    private EditText StartDistance;
    private EditText EndDistance;
    private EditText Area;

    private Button Filter;
    private String passingID;
    private String Query;

    public static final String EXTRA_TEXT = "com.example.javasouls2.FilterMenuBoardings.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter_menu_boardings);

        TitleTop = (TextView) findViewById(R.id.Title);
        DrawerLeft = (ImageView) findViewById(R.id.toolBarFilter);
        AccountIcon = (ImageView) findViewById(R.id.editAccountBtnFilter);

        ForBoys = (RadioButton) findViewById(R.id.ForBoysRadio);
        ForGirls = (RadioButton) findViewById(R.id.ForGirlsRadio);

        ElectricityYes = (RadioButton) findViewById(R.id.ElectricityBillYes);
        ElectricityNo = (RadioButton) findViewById(R.id.ElectricityBillNo);

        WaterBillYes = (RadioButton) findViewById(R.id.WaterBillYes);
        WaterBilNo = (RadioButton) findViewById(R.id.WaterBillNo);

        BathroomsYes = (RadioButton) findViewById(R.id.AttachedBathroomsYes);
        BathroomsNo = (RadioButton) findViewById(R.id.AttachedBathroomsNo);

        KitchenYes = (RadioButton) findViewById(R.id.KitchenYesFilter);
        KitchenNo = (RadioButton) findViewById(R.id.KitchenNoFilter);

        StartRental = (EditText) findViewById(R.id.PriceFrom);
        EndRental = (EditText) findViewById(R.id.PriceTo);

        Beds = (EditText) findViewById(R.id.NoOfBedsFilter);

        StartKeyMoney = (EditText) findViewById(R.id.KeyMoneyFrom);
        EndKeyMoney = (EditText) findViewById(R.id.KeyMoneyTo);

        StartDistance = (EditText) findViewById(R.id.DistanceFrom);
        EndDistance = (EditText) findViewById(R.id.DistanceTo);
        Area = (EditText) findViewById(R.id.AreaFilter);

        Filter = (Button) findViewById(R.id.FilterBtn);

        SessionManager sessionManager = new SessionManager(FilterMenuBoardings.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        ForBoys.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueGender = "Male";
            }
        });

        ForGirls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueGender = "Female";
            }
        });

        ElectricityYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueElectricity = "Yes";
            }
        });

        ElectricityNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueElectricity = "No";
            }
        });

        WaterBillYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueWaterBill = "Yes";
            }
        });

        WaterBilNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueWaterBill = "No";
            }
        });

        BathroomsYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueBathrooms = "Yes";
            }
        });

        BathroomsNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueBathrooms = "No";
            }
        });

        KitchenYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueKitchen = "Yes";
            }
        });

        KitchenNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueKitchen = "No";
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), FilterAndSearchMenuUser.class);
        startActivity(intent);
    }

    public void moveToEditAccount(View view){

        Intent intent = new Intent(getApplicationContext(), EditAccountWindow.class);

        //intent.putExtra(EXTRA_TEXT, passingID);
        startActivity(intent);

    }

    public void FilterGoBoardings(View view){

        FilterData filterData = new FilterData();
        filterData.execute("");

    }

    public class FilterData extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(FilterMenuBoardings.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(FilterMenuBoardings.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FilteredBoardingsWindow.class);

                intent.putExtra(EXTRA_TEXT,Query);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String StartRentalText = StartRental.getText().toString();
            String EndRentalText = EndRental.getText().toString();
            String Rental;
            String NoOfBeds = Beds.getText().toString();
            String BedsQuantity;
            String StartKeyMoneyText = StartKeyMoney.getText().toString();
            String EndKeyMoneyText = EndKeyMoney.getText().toString();
            String KeyMoney;
            String StartDistanceText = StartDistance.getText().toString();
            String EndDistanceText = EndDistance.getText().toString();
            String DistanceFilter;
            String AreaText = Area.getText().toString();
            String CityArea;
            String Gender;
            String Electricity;
            String WaterBill;
            String Bathrooms;
            String Kitchen;

            //ForCompoundButtons
            if (selectedValueGender.equals("")){
                Gender = "";
            }
            else{
                Gender = "and MaleOrFemale = '" + selectedValueGender + "'";
            }

            if (selectedValueElectricity.equals("")){
                Electricity = "";
            }
            else{
                Electricity = "and ElectricityBill = '" + selectedValueElectricity + "'";
            }

            if (selectedValueWaterBill.equals("")){
                WaterBill = "";
            }
            else{
                WaterBill = "and WaterBill = '" + selectedValueWaterBill + "'";
            }

            if (selectedValueBathrooms.equals("")){
                Bathrooms = "";
            }
            else{
                Bathrooms = "and AttachedBathroom = '" + selectedValueBathrooms + "'";
            }

            if (selectedValueKitchen.equals("")){
                Kitchen = "";
            }
            else{
                Kitchen = "and Kitchen = '" + selectedValueKitchen + "'";
            }


            //FilterByRental
            if (StartRentalText.equals("")){

                if (EndRentalText.equals("")){
                    Rental = "";
                }
                else{
                    Rental = "and RentalPerPerson <= " + EndRentalText;
                }
            }
            else {

                if (EndRentalText.equals("")){
                    Rental = "and RentalPerPerson >= " + StartRentalText;
                }
                else {
                    Rental = "and RentalPerPerson between " + StartRentalText + " and " + EndRentalText;
                }

            }

            //FilterByNoOfBeds
            if (NoOfBeds.equals("")){
                BedsQuantity = "";
            }
            else{
                BedsQuantity = "and NoOfBeds = " + NoOfBeds;
            }

            //FilterByKeyMoney
            if (StartKeyMoneyText.equals("")){

                if (EndKeyMoneyText.equals("")){
                    KeyMoney = "";
                }
                else{
                    KeyMoney = "and KeyMoney <= " + EndKeyMoneyText;
                }
            }
            else {

                if (EndKeyMoneyText.equals("")){
                    KeyMoney = "and KeyMoney >= " + StartKeyMoneyText;
                }
                else {
                    KeyMoney = "and KeyMoney between " + StartKeyMoneyText + " and " + EndKeyMoneyText;
                }

            }

            //FilterByDistance
            if (StartDistanceText.equals("")){

                if (EndDistanceText.equals("")){
                    DistanceFilter = "";
                }
                else{
                    DistanceFilter = "and DistanceFromTheUniversity <= " + EndDistanceText;
                }
            }
            else {

                if (EndDistanceText.equals("")){
                    DistanceFilter = "and DistanceFromTheUniversity >= " + StartDistanceText;
                }
                else {
                    DistanceFilter = "and DistanceFromTheUniversity between " + StartDistanceText + " and " + EndDistanceText;
                }

            }

            //FilterByCity
            if (AreaText.equals("")){
                CityArea = "";
            }
            else{
                CityArea = "and City = '" + AreaText + "'";
            }

            Query = "select * from BoardingHouseRooms,BoardingHouse where IsOccupied = 'No' " + Gender + " " + Electricity + " " + WaterBill + " " + Bathrooms + " " + Kitchen + " " + Rental + " " + BedsQuantity + " " + KeyMoney + " " + DistanceFilter + " " + CityArea + ";";

            isSuccess = true;
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