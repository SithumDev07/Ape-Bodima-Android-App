package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasouls2.User.FilterAndSearchMenuUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class BoardingResults extends AppCompatActivity implements DatabaseCredentials{

    private ImageView Account;
    private ImageView DrawerButton;
    private TextView welcomeTitle;
    private RelativeLayout searchBar;
    private LinearLayout mainButtons;

    private String passingID;
    String SelectedItemID;
    private Connection connection;

    private RecyclerView BoardingRecycler;
    private StaticRecyclerViewAdapter BoardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_boarding_results);

        Account = (ImageView) findViewById(R.id.editAccountBtn);
        DrawerButton = (ImageView) findViewById(R.id.toolBar);
        searchBar = (RelativeLayout) findViewById(R.id.SearchBarResultsBoardings);
        mainButtons= (LinearLayout) findViewById(R.id.MainTwoButtons);
        welcomeTitle = (TextView) findViewById(R.id.WelcomeText);

        SessionManager sessionManager = new SessionManager(BoardingResults.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        GetPreDataBoarding getPreDataBoarding = new GetPreDataBoarding();
        getPreDataBoarding.execute("");
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
        startActivity(intent);

    }

    public void MoveToEditAccountUser(View view){


        Intent intentToEdit = new Intent(getApplicationContext(), EditAccountWindow.class);
        startActivity(intentToEdit);

    }

    public void MoveToAnnexResults(View view){

        Intent moveToAnxResults = new Intent(getApplicationContext(),AnnexesResult.class);

        /*Pair[] pairs = new Pair[5];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(DrawerButton, "drawer");
        pairs[2] = new Pair<View, String>(welcomeTitle, "WelcomeText");
        pairs[3] = new Pair<View, String>(searchBar, "Searchbar");
        pairs[4] = new Pair<View, String>(mainButtons, "MainButtons");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(BoardingResults.this, pairs);*/
        startActivity(moveToAnxResults);

    }

    public class GetPreDataBoarding extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        ArrayList<StaticRecyclerViewModel> Boardings = new ArrayList<>();
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
                        String query = "SELECT * FROM AppUser WHERE Username = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";
                            String FullNameText = "Welcome " + resultSet.getString("FirstName");
                            welcomeTitle.setText(FullNameText);

                            String queryFetchData = "select BoardingHouseRooms.RoomID,BoardingHouseRooms.BoardingID,BoardingHouse.BoardingID,BoardingHouse.City,BoardingHouseRooms.IsOccupied,BoardingHouseRooms.NoOfBeds,BoardingHouseRooms.MaleOrFemale,BoardingHouseRooms.RentalPerPerson from BoardingHouseRooms,BoardingHouse where BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID and IsOccupied = 'No';";
                            ResultSet resultSetFetchAnnexes = statement.executeQuery(queryFetchData);



                            float rating = 0.0f;
                            String gender;
                            String BoardingFor;
                            String RoomID;
                            while (resultSetFetchAnnexes.next()) {

                                RoomID = resultSetFetchAnnexes.getString("RoomID");

                                gender = resultSetFetchAnnexes.getString("MaleOrFemale");

                                if (gender.equals("Male"))
                                    BoardingFor = "Boarding for boys";
                                else
                                    BoardingFor = "Boarding for girls";


                                String queryOverallRating = "select avg(Rating) as Rating,RoomID from BoardingRoomRating where RoomID = '" + RoomID + "' group by RoomID;";
                                Statement statementRating = connection.createStatement();
                                ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                                if(resultSetRating.next()){
                                    rating = Float.parseFloat(resultSetRating.getString("Rating"));
                                }
                                Boardings.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Boarding in " + resultSetFetchAnnexes.getString("City"), "Beds : " + resultSetFetchAnnexes.getString("NoOfBeds"), BoardingFor, "Rs." + resultSetFetchAnnexes.getString("RentalPerPerson"), rating,resultSetFetchAnnexes.getString("RoomID"),"0"));

                            }
                            BoardingRecycler = findViewById(R.id.RecyclerView3);
                            BoardingAdapter = new StaticRecyclerViewAdapter(Boardings,this);
                            BoardingRecycler.setLayoutManager(new LinearLayoutManager(BoardingResults.this, LinearLayoutManager.HORIZONTAL, false));
                            BoardingRecycler.setAdapter(BoardingAdapter);

                            isSuccess = true;
                            //connection.close();
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

        @Override
        public void onItemClick(int position) {

            SelectedItemID = Boardings.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(BoardingResults.this,SessionManager.SESSION_REMEMBERME_BOARDING);
            sessionManager.createRememberMeSessionBoarding(SelectedItemID);



            Intent intentToAd = new Intent(getApplicationContext(),AdViewUserBoarding.class);

            //intentToAd.putExtra(EXTRA_TEXT,SelectedItemID);

            startActivity(intentToAd);

        }
    }

    public void GoSearchAndFilter(View view){

        Intent moveToAnxResults = new Intent(getApplicationContext(), FilterAndSearchMenuUser.class);

        Pair[] pairs = new Pair[3];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(DrawerButton, "drawer");
        pairs[2] = new Pair<View, String>(searchBar, "Searchbar");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(BoardingResults.this, pairs);
        startActivity(moveToAnxResults, options.toBundle());


    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server, String port){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");


           // ConnectionURL = "jdbc:jtds:sqlserver://apebodima.database.windows.net:1433;DatabaseName=ApeBodima;user=adminApeBodima@apebodima;password=Fr13nds2552;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";


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