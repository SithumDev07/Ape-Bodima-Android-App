package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasouls2.User.FilterAndSearchMenuUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class FilterMenuAnnexes extends AppCompatActivity implements DatabaseCredentials{

    private Connection connection;

    private TextView TitleTop;
    private ImageView DrawerLeft;
    private ImageView AccountIcon;

    private String selectedValueFurnished = "";
    private RadioButton FurnishedYes;
    private RadioButton FurnishedNo;

    private EditText StartRental;
    private EditText EndRental;
    private EditText Rooms;
    private EditText StartKeyMoney;
    private EditText EndKeyMoney;
    private EditText StartDistance;
    private EditText EndDistance;
    private EditText Area;
    private EditText Bathrooms;

    private Button Filter;
    private String passingID;
    private String Query;

    public static final String EXTRA_TEXT = "com.example.javasouls2.FilterMenuAnnexes.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter_menu_annexes);

        TitleTop = (TextView) findViewById(R.id.TitleAnnex);
        DrawerLeft = (ImageView) findViewById(R.id.toolBarFilterAnnex);
        AccountIcon = (ImageView) findViewById(R.id.editAccountBtnFilterAnnex);

        FurnishedYes = (RadioButton) findViewById(R.id.FurnishedYesRadioAnnex);
        FurnishedNo = (RadioButton) findViewById(R.id.FurnishedNoRadioAnnex);

        StartRental = (EditText) findViewById(R.id.PriceFromAnnex);
        EndRental = (EditText) findViewById(R.id.PriceToAnnex);

        Rooms = (EditText) findViewById(R.id.RoomsFilterAnnex);
        Bathrooms = (EditText) findViewById(R.id.BathroomsFilterAnnex);

        StartKeyMoney = (EditText) findViewById(R.id.KeyMoneyFromAnnex);
        EndKeyMoney = (EditText) findViewById(R.id.KeyMoneyToAnnex);

        StartDistance = (EditText) findViewById(R.id.DistanceFromAnnex);
        EndDistance = (EditText) findViewById(R.id.DistanceToAnnex);
        Area = (EditText) findViewById(R.id.AreaFilterAnnex);

        Filter = (Button) findViewById(R.id.FilterBtnAnnex);

        SessionManager sessionManager = new SessionManager(FilterMenuAnnexes.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        FurnishedYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueFurnished = "Yes";
            }
        });

        FurnishedNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedValueFurnished = "No";
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
        startActivity(intent);

    }

    public void FilterGoAnnexes(View view){

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
            Toast.makeText(FilterMenuAnnexes.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(FilterMenuAnnexes.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FilteredAnnexesWindow.class);

                intent.putExtra(EXTRA_TEXT,Query);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String StartRentalText = StartRental.getText().toString();
            String EndRentalText = EndRental.getText().toString();
            String Rental;
            String RoomsText = Rooms.getText().toString();
            String RoomsQuantity;
            String BathroomsText = Bathrooms.getText().toString();
            String BathroomsQuantity;
            String StartKeyMoneyText = StartKeyMoney.getText().toString();
            String EndKeyMoneyText = EndKeyMoney.getText().toString();
            String KeyMoney;
            String StartDistanceText = StartDistance.getText().toString();
            String EndDistanceText = EndDistance.getText().toString();
            String DistanceFilter;
            String AreaText = Area.getText().toString();
            String CityArea;
            String Furnished;

            //ForCompoundButtons
            if (selectedValueFurnished.equals("")){
                Furnished = "";
            }
            else{
                Furnished = "and IsFurnished = '" + selectedValueFurnished + "'";
            }

            //FilterByRental
            if (StartRentalText.equals("")){

                if (EndRentalText.equals("")){
                    Rental = "";
                }
                else{
                    Rental = "and RentalPerMonth <= " + EndRentalText;
                }
            }
            else {

                if (EndRentalText.equals("")){
                    Rental = "and RentalPerMonth >= " + StartRentalText;
                }
                else {
                    Rental = "and RentalPerMonth between " + StartRentalText + " and " + EndRentalText;
                }

            }

            //FilterByNoOfBeds
            if (RoomsText.equals("")){
                RoomsQuantity = "";
            }
            else{
                RoomsQuantity = "and NoOfRooms = " + RoomsText;
            }

            //FilterByBathrooms
            if (BathroomsText.equals("")){
                BathroomsQuantity = "";
            }
            else{
                BathroomsQuantity = "and NoOfBathrooms = " + BathroomsText;
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

            Query = "select * from Annex where IsOccupied = 'No' " + Furnished + " " + Rental + " " + RoomsQuantity + " " + BathroomsQuantity + " " + KeyMoney + " " + DistanceFilter + " " + CityArea + ";";

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