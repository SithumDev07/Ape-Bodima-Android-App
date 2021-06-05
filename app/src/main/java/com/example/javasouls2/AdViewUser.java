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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdViewUser extends AppCompatActivity implements DatabaseCredentials{

    private ViewPager2 viewPager2;
    private Handler Slidehandler = new Handler();

    private Connection connection;
    //Demo
    private String AnnexID;

    private TextView AnnexLocation;
    private TextView RentalPerMonth;
    private TextView Rooms;
    private TextView AddressLine1;
    private TextView AddressLine2;
    private TextView City;
    private TextView IsFurnished;
    private TextView KeyMoneyMonths;
    private TextView KeyMoney;
    private TextView Bathrooms;
    private TextView Distance;
    private TextView PhoneNumber;

    private Button Help;

    private RatingBar ratingBar;

    private Bitmap bitmapImageDB;
    private byte[] byteImageDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ad_view_user_re_arranged);

        AnnexLocation = (TextView) findViewById(R.id.LocationAnnexesCity);
        RentalPerMonth = (TextView) findViewById(R.id.RentalPerMonthAnnexAd);
        Rooms = (TextView) findViewById(R.id.NoOfRoomsAnnexAd);
        AddressLine1 = (TextView) findViewById(R.id.AddressLine1AnnexAd);
        AddressLine2 = (TextView) findViewById(R.id.AddressLine2AnnexAd);
        City = (TextView) findViewById(R.id.AddressCityAnnexAd);
        IsFurnished = (TextView) findViewById(R.id.IsFurnishedAnnexAd);
        KeyMoneyMonths = (TextView) findViewById(R.id.KeyMoneyMonthsAnnexAd);
        KeyMoney = (TextView) findViewById(R.id.KeyMoneyAnnexAd);
        Bathrooms = (TextView) findViewById(R.id.NoOfBathroomsAnnexAd);
        Distance = (TextView) findViewById(R.id.DistanceAnnexAd);
        PhoneNumber = (TextView) findViewById(R.id.PhoneNumberAdAnnex);

        Help = (Button) findViewById(R.id.HelpBtnAnnexAd);

        ratingBar = (RatingBar) findViewById(R.id.RatingAnnexAd);

        viewPager2 = findViewById(R.id.LocationsViewAnnexes);

        SessionManager sessionManager = new SessionManager(AdViewUser.this, SessionManager.SESSION_REMEMBERME_ANNEX);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsAnnexFromSession();
        AnnexID = rememberMeDetails.get(SessionManager.KEY_SESSION_ANNEXID);

        //Intent intentData = getIntent();
        //AnnexID = intentData.getStringExtra(AnnexesResult.EXTRA_TEXT);

        getPreDataAnnexAd getPreData = new getPreDataAnnexAd();
        getPreData.execute("");

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

    public class getPreDataAnnexAd extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {


            if (AnnexID.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {


                        ArrayList<LocationPhotoAd> locationPhotoAds = new ArrayList<>();


                        String fetchAnnexAdPicture = "select * from AnnexPhoto where AnnexID = '" + AnnexID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSetImage = statement.executeQuery(fetchAnnexAdPicture);
                        while(resultSetImage.next()){

                            byteImageDb = resultSetImage.getBytes("Photo");

                            bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);

                            locationPhotoAds.add(new LocationPhotoAd(bitmapImageDB));

                        }

                        viewPager2.setAdapter(new LocationPhotoAdapter(locationPhotoAds,viewPager2));

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


                        String queryGetPhone = "select top 1 ClientPhoneNumber.PhoneNumber,ClientPhoneNumber.NIC,Annex.NIC from Annex,ClientPhoneNumber where Annex.AnnexID = '" + AnnexID + "' and ClientPhoneNumber.NIC = Annex.NIC;";
                        ResultSet resultSetPhone = statement.executeQuery(queryGetPhone);

                        resultSetPhone.next();

                        PhoneNumber.setText("Contact : " + resultSetPhone.getString("PhoneNumber"));

                        String query = "select * from Annex where AnnexID = '" + AnnexID + "';";

                        ResultSet resultSet = statement.executeQuery(query);
                        float rating ;

                        if (resultSet.next()) {
                            z = "Login Successful";

                            AnnexLocation.setText("Annex in " + resultSet.getString("City"));
                            RentalPerMonth.setText("Rs." + resultSet.getString("RentalPerMonth"));
                            Rooms.setText("Rooms : " + resultSet.getString("NoOfRooms"));
                            AddressLine1.setText("Address : " + resultSet.getString("AddressLine1"));
                            AddressLine2.setText(resultSet.getString("AddressLine2"));
                            City.setText(resultSet.getString("City"));
                            IsFurnished.setText("Furnished : " + resultSet.getString("IsFurnished"));
                            KeyMoneyMonths.setText("Key money for " + resultSet.getString("KeyMoneyMonths") + " months");
                            KeyMoney.setText("Key money : Rs." + resultSet.getString("KeyMoney"));
                            Bathrooms.setText("Bathrooms : " + resultSet.getString("NoOfBathrooms"));
                            Distance.setText("Distance from the university : " + resultSet.getString("DistanceFromTheUniversity")+ "m");


                            String queryOverallRating = "select avg(Rating) as Rating,AnnexID from AnnexRating where AnnexID = '" + AnnexID + "' group by AnnexID;";
                            Statement statementRating = connection.createStatement();
                            ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                            if (resultSetRating.next()) {
                                rating = Float.parseFloat(resultSetRating.getString("Rating"));
                            } else {
                                rating = 0.0f;
                            }

                            ratingBar.setRating(rating);



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

    public void BacktoWhere(View view){

        Intent intent = new Intent(getApplicationContext(),UserDashboard.class);
        startActivity(intent);

    }


    public void MoveToEditAccountWindow(View view){
        Intent intentToEdit = new Intent(getApplicationContext(), EditAccountWindow.class);
        startActivity(intentToEdit);
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