package com.example.javasouls2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class AdViewUserBoarding extends AppCompatActivity implements DatabaseCredentials{

    private ViewPager2 viewPager2;
    private Handler Slidehandler = new Handler();

    private Connection connection;
    //Demo
    private String BoardingID;

    private TextView BoardingLocation;
    private TextView RentalPerMonthBoarding;
    private TextView Beds;
    private TextView AddressLine1;
    private TextView AddressLine2;
    private TextView City;

    private TextView KeyMoneyMonths;
    private TextView KeyMoney;
    private TextView Bathrooms;

    private TextView ElectricityBill;
    private TextView WaterBill;
    private TextView Kitchen;
    private TextView Distance;
    private TextView PhoneNumber;

    String passingID;

    private RatingBar ratingBar;

    private Bitmap bitmapImageDB;
    private byte[] byteImageDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ad_view_user_boarding);

        BoardingLocation = (TextView) findViewById(R.id.LocationBoarding);
        RentalPerMonthBoarding = (TextView) findViewById(R.id.RentalPerMonthBoarding);
        Beds = (TextView) findViewById(R.id.NoOfBeds);

        AddressLine1 = (TextView) findViewById(R.id.AddressLine1Boarding);
        AddressLine2 = (TextView) findViewById(R.id.AddressLine2Boarding);
        City = (TextView) findViewById(R.id.AddressCityBoarding);

        KeyMoneyMonths = (TextView) findViewById(R.id.KeyMoneyMonthsBoarding);
        KeyMoney = (TextView) findViewById(R.id.KeyMoneyBoarding);
        Bathrooms = (TextView) findViewById(R.id.BathroomsBoarding);
        ElectricityBill = (TextView) findViewById(R.id.ElectricityBillBoarding);
        WaterBill = (TextView) findViewById(R.id.WaterBillBoarding);
        Kitchen = (TextView) findViewById(R.id.KitchenBoarding);

        Distance = (TextView) findViewById(R.id.DistanceBoarding);
        PhoneNumber = (TextView) findViewById(R.id.PhoneNumberAdBoarding);

        ratingBar = (RatingBar) findViewById(R.id.RatingBoardingAd);

        viewPager2 = findViewById(R.id.LocationsViewBoarding);

        SessionManager sessionManagerUser = new SessionManager(AdViewUserBoarding.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetailsUser = sessionManagerUser.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetailsUser.get(SessionManager.KEY_SESSIONUSER);

        SessionManager sessionManager = new SessionManager(AdViewUserBoarding.this, SessionManager.SESSION_REMEMBERME_BOARDING);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsBoardingFromSession();
        BoardingID = rememberMeDetails.get(SessionManager.KEY_SESSIONBOARDINGID);

        //Intent intentData = getIntent();
        //AnnexID = intentData.getStringExtra(AnnexesResult.EXTRA_TEXT);

        getContentDataBoardingAd getContentBoarding = new getContentDataBoardingAd();
        getContentBoarding.execute("");

        getPreDataBoardingAd getPreBoarding = new getPreDataBoardingAd();
        getPreBoarding.execute("");

    }




    public class getPreDataBoardingAd extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {


            if (BoardingID.trim().equals("")) {

                z = "Fatal Error - ID not found";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "Turn on cellular data or Wi-Fi";
                    } else {

                        String test = "select * from AppUser where Username = '" + passingID + "'";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(test);


                        if(resultSet.next()){

                            ArrayList<LocationPhotoAd> locationPhotoAdsBoarding = new ArrayList<>();


                            String fetchAnnexAdPicture = "select * from BoardingHouseRoomsPhoto where RoomID = '" + BoardingID + "';";

                            ResultSet resultSetImage = statement.executeQuery(fetchAnnexAdPicture);
                            while(resultSetImage.next()){

                                byteImageDb = resultSetImage.getBytes("Photo");

                                bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);

                                locationPhotoAdsBoarding.add(new LocationPhotoAd(bitmapImageDB));

                            }

                            viewPager2.setAdapter(new LocationPhotoAdapter(locationPhotoAdsBoarding,viewPager2));

                            viewPager2.setClipToPadding(false);
                            viewPager2.setClipChildren(false);
                            viewPager2.setOffscreenPageLimit(3);
                            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();

                            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                @Override
                                public void transformPage(@NonNull View page, float position) {
                                    float r = 1 - Math.abs(position);
                                    page.setScaleY(0.85f + r * 0.15f);
                                }
                            });

                            viewPager2.setPageTransformer(compositePageTransformer);

                            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                @Override
                                public void onPageSelected(int position) {
                                    super.onPageSelected(position);

                                    Slidehandler.removeCallbacks(sliderRunable);
                                    Slidehandler.postDelayed(sliderRunable,3000);
                                }
                            });



                            isSuccess = true;
                            connection.close();

                        }
                        else {
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
    public void BacktoWhere(View view){
        Intent intent = new Intent(getApplicationContext(),UserDashboard.class);
        startActivity(intent);
    }

    public void MoveToEditAccountWindow(View view){
        Intent intent = new Intent(getApplicationContext(),EditAccountWindow.class);
        startActivity(intent);
    }



    public class getContentDataBoardingAd extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {


            if (BoardingID.trim().equals("")) {

                z = "Fatal Error - ID not found";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "Turn on cellular data or Wi-Fi";
                    } else {


                        Statement statement = connection.createStatement();

                        String query = "select BoardingHouseRooms.RoomID,BoardingHouse.AddressLine1,BoardingHouse.AddressLine2,BoardingHouse.AddressLine3,BoardingHouse.City,BoardingHouse.DistanceFromTheUniversity,BoardingHouseRooms.NoOfBeds,BoardingHouseRooms.MaleOrFemale,BoardingHouseRooms.WaterBill,BoardingHouseRooms.KeyMoneyMonths,BoardingHouseRooms.KeyMoney,BoardingHouseRooms.RentalPerPerson,BoardingHouseRooms.AttachedBathroom,BoardingHouseRooms.Kitchen,BoardingHouseRooms.ElectricityBill from BoardingHouse,BoardingHouseRooms where BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID and BoardingHouseRooms.RoomID = '" + BoardingID + "';";

                        ResultSet resultSetContent = statement.executeQuery(query);
                        String BoardingFor;
                        float Rating;

                        if (resultSetContent.next()) {
                            z = "Login Successful";

                            BoardingFor = resultSetContent.getString("MaleOrFemale");
                            if(BoardingFor.equals("Male")){
                                BoardingLocation.setText("Boarding in " + resultSetContent.getString("City") + " for Boys");
                            }
                            if(BoardingFor.equals("Female")){
                                BoardingLocation.setText("Boarding in " + resultSetContent.getString("City") + " for Girls");
                            }

                            RentalPerMonthBoarding.setText("Rs." + resultSetContent.getString("RentalPerPerson"));
                            Beds.setText("Beds : " + resultSetContent.getString("NoOfBeds"));

                            AddressLine1.setText("Address : " + resultSetContent.getString("AddressLine1"));
                            AddressLine2.setText(resultSetContent.getString("AddressLine2") + ", " + resultSetContent.getString("AddressLine3"));
                            City.setText(resultSetContent.getString("City"));

                            KeyMoneyMonths.setText("Key money for " + resultSetContent.getString("KeyMoneyMonths") + " months");
                            KeyMoney.setText("Key money : Rs." + resultSetContent.getString("KeyMoney"));
                            Bathrooms.setText("Attached Bathrooms : " + resultSetContent.getString("AttachedBathroom"));
                            ElectricityBill.setText("Electricity Bill Included : " + resultSetContent.getString("ElectricityBill"));
                            WaterBill.setText("Water Bill Included : " + resultSetContent.getString("WaterBill"));
                            Kitchen.setText("Kitchen : " + resultSetContent.getString("Kitchen"));
                            Distance.setText("Distance from the university : " + resultSetContent.getString("DistanceFromTheUniversity") + "m");

                            String queryOverallRating = "select avg(Rating) as Rating,RoomID from BoardingRoomRating where RoomID = '" + BoardingID + "' group by RoomID;";
                            Statement statementRating = connection.createStatement();
                            ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                            if(resultSetRating.next()){
                                Rating = Float.parseFloat(resultSetRating.getString("Rating"));

                            }
                            else{
                                Rating = 0.0f;
                            }

                            ratingBar.setRating(Rating);

                            String queryGetPhone = "select top 1 BoardingHouse.BoardingID,BoardingHouse.NIC,ClientPhoneNumber.NIC,BoardingHouseRooms.RoomID,ClientPhoneNumber.PhoneNumber from BoardingHouse,BoardingHouseRooms,ClientPhoneNumber where BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID and BoardingHouse.NIC = ClientPhoneNumber.NIC and BoardingHouseRooms.RoomID = '" + BoardingID + "';";
                            ResultSet resultSetPhone = statement.executeQuery(queryGetPhone);

                            resultSetPhone.next();

                            PhoneNumber.setText("Contact : " + resultSetPhone.getString("PhoneNumber"));


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


    private Runnable sliderRunable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        Slidehandler.removeCallbacks(sliderRunable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Slidehandler.postDelayed(sliderRunable,3000);
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