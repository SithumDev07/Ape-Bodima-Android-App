package com.example.javasouls2;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.javasouls2.Client.AddBoardingHouseWindow;
import com.example.javasouls2.Client.AllAnnexes;
import com.example.javasouls2.Client.AllBoardings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ClientPanel extends AppCompatActivity implements DatabaseCredentials {

    //Variables
    private TextView BoardingTitle;
    private TextView AnnexTitleMain;
    private TextView BedsBr;
    private TextView PerMonthBr;
    private TextView BoysOrGirls;
    private Button addBr;
    private RatingBar ratingBr;

    private TextView NothingBr;
    private TextView NothingAnx;

    private TextView AnnexTitle;
    private TextView RoomsAnx;
    private TextView PerMonthAnx;
    private TextView AvailabilityAnx;
    private de.hdodenhof.circleimageview.CircleImageView AnnexAdPhoto;
    private de.hdodenhof.circleimageview.CircleImageView BoardingAdPhoto;
    private Button addAnx;
    private RatingBar ratingAnx;
    private Connection connection;

    private TextView adTitle;

    private TextView NoOfBoardingAds;
    private TextView NoOfAnnexAds;
    private String passingID;
    private Statement statement;
    private String BoardingID;
    private String AnnexID;
    private String RoomID;
    public static final String EXTRA_TEXT = "com.example.javasouls2.ClientPanel.EXTRA_TEXT";

    private com.chinodev.androidneomorphframelayout.NeomorphFrameLayout layoutBoarding;
    private com.chinodev.androidneomorphframelayout.NeomorphFrameLayout layoutAnnex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_panel);

        layoutBoarding = (com.chinodev.androidneomorphframelayout.NeomorphFrameLayout) findViewById(R.id.BoardingAD);
        layoutAnnex = (com.chinodev.androidneomorphframelayout.NeomorphFrameLayout) findViewById(R.id.AnnexAD);

        //Hooks
        BoardingTitle = (TextView) findViewById(R.id.BoardingText);
        AnnexTitleMain = (TextView) findViewById(R.id.AnnexText);
        addBr = (Button) findViewById(R.id.AddBoarding);
        addAnx = (Button) findViewById(R.id.AddAnnex);

        NothingBr = (TextView) findViewById(R.id.NothingBoardings);
        NothingBr.setVisibility(View.GONE);

        NothingAnx = (TextView) findViewById(R.id.NothingAnnexes);
        NothingAnx.setVisibility(View.GONE);

        NoOfBoardingAds = (TextView) findViewById(R.id.NOOfBoardingsAdsView);
        NoOfAnnexAds = (TextView) findViewById(R.id.NoOfAnnexAdsView);

        adTitle = (TextView) findViewById(R.id.TitleBoarding);


        BedsBr = (TextView) findViewById(R.id.NoOfBedsBR);
        PerMonthBr = (TextView) findViewById(R.id.RentalPerMonthBR);
        BoysOrGirls = (TextView) findViewById(R.id.MaleOrFemale);
        ratingBr = (RatingBar) findViewById(R.id.Ratingad1);

        AnnexTitle = (TextView) findViewById(R.id.AdTextAnx);
        RoomsAnx = (TextView) findViewById(R.id.NoOfRoomsANX);
        AvailabilityAnx = (TextView) findViewById(R.id.IsAvailable);
        PerMonthAnx = (TextView) findViewById(R.id.RentalPerMonthANX);
        ratingAnx = (RatingBar) findViewById(R.id.Ratingad2);

        AnnexAdPhoto = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.AnnexAdImage);
        BoardingAdPhoto = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.BoardingAdImage);

        SessionManager sessionManager = new SessionManager(ClientPanel.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        getPreData getData = new getPreData();
        getData.execute("");

        getPreDataAnnexAd annexDetails = new getPreDataAnnexAd();
        annexDetails.execute("");

        addBr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");
            }
        });

        addAnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnexClick annexClick = new AnnexClick();
                annexClick.execute("");
            }
        });

        layoutBoarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(ClientPanel.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure want to delete this boarding?")
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

                                String queryDelete = "delete from BoardingHouse where BoardingID = '" + BoardingID + "';";
                                Statement statement = connection.createStatement();
                                statement.executeUpdate(queryDelete);

                            }

                            connection.close();
                        } catch (Exception e) {

                        }

                        Toast.makeText(ClientPanel.this, "Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ClientPanel.class);

                        startActivity(intent);
                    }
                });
            }
        });

    }

    public void MoveToAllBoardings(View view){

        Intent intent = new Intent(getApplicationContext(), AllBoardings.class);
        startActivity(intent);

    }

    public void MoveToAllAnnexes(View view){

        Intent intent = new Intent(getApplicationContext(), AllAnnexes.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), SearchMenu.class);
        startActivity(intent);

    }


    public class getPreData extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;
        float rating;

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
                        String query = "SELECT TOP 1 * FROM BoardingHouse WHERE NIC = '" + passingID + "' order by BoardingID desc;";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);


                        if (resultSet.next()) {
                            z = "Login Successfull";

                            BoardingID = resultSet.getString("BoardingID");
                            adTitle.setText("Boarding in " + resultSet.getString("City"));


                            String queryRooms = "SELECT TOP 1 * FROM BoardingHouseRooms WHERE BoardingID = '" + resultSet.getString("BoardingID") + "' order by RoomID desc;";
                            ResultSet resultSet1 = statement.executeQuery(queryRooms);

                            if (resultSet1.next()) {

                                RoomID = resultSet1.getString("RoomID");

                                PerMonthBr.setText("Rental per month : Rs." + resultSet1.getString("RentalPerPerson"));


                                BedsBr.setText("Beds : " + resultSet1.getString("NoOfBeds"));

                                String GenderFromRooms = resultSet1.getString("MaleOrFemale");


                                if (GenderFromRooms.equals("Male")) {
                                    BoysOrGirls.setText("Boarding for Boys");
                                } else
                                    BoysOrGirls.setText("Boarding for Girls");



                                String queryBoardingAds = "SELECT count(BoardingID) AS Count FROM BoardingHouse where NIC = '" + passingID + "';";
                                ResultSet resultSet2 = statement.executeQuery(queryBoardingAds);

                                if(resultSet2.next()){
                                    NoOfBoardingAds.setText(resultSet2.getString("Count"));
                                }
                                else{
                                    NoOfBoardingAds.setText("0");
                                }

                                String fetchBoardingAdPicture = "select top 1 * from BoardingHouseRoomsPhoto where RoomID = '" + RoomID + "' order by PhotoID asc;";
                                ResultSet resultSetImageBoarding = statement.executeQuery(fetchBoardingAdPicture);
                                resultSetImageBoarding.next();

                                byte[] byteImageDb = resultSetImageBoarding.getBytes("Photo");

                                Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                                BoardingAdPhoto.setImageBitmap(bitmapImageDB);

                                String queryOverallRating = "select avg(Rating) as Rating,RoomID from BoardingRoomRating where RoomID = '" + RoomID + "' group by RoomID;";
                                Statement statementRating = connection.createStatement();
                                ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                                if(resultSetRating.next()){
                                    rating = Float.parseFloat(resultSetRating.getString("Rating"));

                                    ratingBr.setRating(rating);
                                }

                                isSuccess = true;
                                connection.close();

                            }
                        } else {

                            layoutBoarding.setVisibility(View.GONE);
                            adTitle.setVisibility(View.GONE);
                            BoysOrGirls.setVisibility(View.GONE);
                            BoardingAdPhoto.setVisibility(View.GONE);
                            BedsBr.setVisibility(View.GONE);
                            ratingBr.setVisibility(View.GONE);
                            PerMonthBr.setVisibility(View.GONE);
                            NothingBr.setVisibility(View.VISIBLE);

                            z = "Nothing to show";
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


    public class getPreDataAnnexAd extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;
        float rating;

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
                        String query = "SELECT TOP 1 * FROM Annex WHERE NIC = '" + passingID + "' order by AnnexID desc;";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successfull";

                            AnnexID = resultSet.getString("AnnexID");
                            AnnexTitle.setText("Annex in " + resultSet.getString("City"));
                            RoomsAnx.setText("No of rooms: " + resultSet.getString("NoOfRooms"));
                            PerMonthAnx.setText("Rental per month: " + resultSet.getString("RentalPerMonth"));
                            String occupiedDetails = resultSet.getString("IsOccupied");

                            if (occupiedDetails.equals("No")) {
                                AvailabilityAnx.setText("Annex is available for renting");
                            } else
                                AvailabilityAnx.setText("Annex is is not available");

                            String queryAnnexAds = "SELECT count(AnnexID) AS Count FROM Annex where NIC = '" + passingID + "';";
                            ResultSet resultSet3 = statement.executeQuery(queryAnnexAds);

                            if(resultSet3.next()){
                                NoOfAnnexAds.setText(resultSet3.getString("Count"));
                            }
                            else{
                                NoOfAnnexAds.setText("0");
                            }

                            String fetchAnnexAdPicture = "select top 1 * from AnnexPhoto where AnnexID = '" + AnnexID + "' order by PhotoID asc;";
                            ResultSet resultSetImage = statement.executeQuery(fetchAnnexAdPicture);
                            resultSetImage.next();

                            byte[] byteImageDb = resultSetImage.getBytes("Photo");

                            Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                            AnnexAdPhoto.setImageBitmap(bitmapImageDB);

                            String queryOverallRating = "select avg(Rating) as Rating,AnnexID from AnnexRating where AnnexID = '" + AnnexID+ "' group by AnnexID;";
                            Statement statementRating = connection.createStatement();
                            ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                            if(resultSetRating.next()){
                                rating = Float.parseFloat(resultSetRating.getString("Rating"));

                                ratingAnx.setRating(rating);
                            }

                            isSuccess = true;
                            connection.close();
                        } else {

                            layoutAnnex.setVisibility(View.GONE);
                            AnnexTitle.setVisibility(View.GONE);
                            PerMonthAnx.setVisibility(View.GONE);
                            ratingAnx.setVisibility(View.GONE);
                            AnnexAdPhoto.setVisibility(View.GONE);
                            AvailabilityAnx.setVisibility(View.GONE);
                            RoomsAnx.setVisibility(View.GONE);
                            NothingAnx.setVisibility(View.VISIBLE);

                            z = "Nothing to show";
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


    public class CheckLogin extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(ClientPanel.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(ClientPanel.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddBoardingHouseWindow.class);

                Pair[] pairs = new Pair[2];

                //Since we don't need any use of Client Area Button
                pairs[0] = new Pair<View, String>(BoardingTitle, "boarding");
                pairs[1] = new Pair<View, String>(addBr, "addAnother");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientPanel.this, pairs);
                startActivity(intent, options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                connection = connectionclass(User, Pass, DB, IP, PORT);

                if (connection == null) {
                    z = "turn on cellular data or Wi-Fi";
                } else {

                }
                isSuccess = true;
                connection.close();
            } catch (Exception e) {
                isSuccess = false;
                z = e.getMessage();
            }
            return z;
        }
    }


    public class AnnexClick extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccessAnnex = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(ClientPanel.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccessAnnex) {
                Toast.makeText(ClientPanel.this, "Success Moving to create annex window", Toast.LENGTH_LONG).show();
                Intent intentToAnnex = new Intent(getApplicationContext(), AddAnnexWindow.class);

                Pair[] pairs = new Pair[1];

                //Since we don't need any use of Client Area Button
                pairs[0] = new Pair<View,String>(AnnexTitleMain,"annex");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientPanel.this,pairs);
                startActivity(intentToAnnex,options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                connection = connectionclass(User, Pass, DB, IP, PORT);

                if (connection == null) {
                    z = "turn on cellular data or Wi-Fi";
                } else {

                }
                isSuccessAnnex = true;
                connection.close();
            } catch (Exception e) {
                isSuccessAnnex = false;
                z = e.getMessage();
            }
            return z;
        }
    }


    public class DeleteAd extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(ClientPanel.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(ClientPanel.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SearchMenu.class);

                Pair[] pairs = new Pair[2];

                //Since we don't need any use of Client Area Button
                pairs[0] = new Pair<View, String>(BoardingTitle, "boarding");
                pairs[1] = new Pair<View, String>(addBr, "addAnother");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientPanel.this, pairs);
                //intent.putExtra(EXTRA_TEXT,passingID);
                startActivity(intent, options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (passingID.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "delete from BoardingHouse where NIC = '" + passingID + "' and BoardingID = '" + BoardingID + "';";
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