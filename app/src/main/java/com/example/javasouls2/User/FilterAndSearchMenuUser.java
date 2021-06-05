package com.example.javasouls2.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.javasouls2.AdViewUser;
import com.example.javasouls2.AdViewUserBoarding;
import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.EditAccountWindow;
import com.example.javasouls2.FilterMenuAnnexes;
import com.example.javasouls2.FilterMenuBoardings;
import com.example.javasouls2.FilteredAnnexesWindow;
import com.example.javasouls2.FilteredBoardingsWindow;
import com.example.javasouls2.R;
import com.example.javasouls2.RecyclerViewClickInterface;
import com.example.javasouls2.SessionManager;
import com.example.javasouls2.StaticRecyclerViewAdapter;
import com.example.javasouls2.StaticRecyclerViewModel;
import com.example.javasouls2.UserDashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterAndSearchMenuUser extends AppCompatActivity implements DatabaseCredentials {

    private String passingID;
    private Connection connection;

    private RecyclerView annexRecycler;
    private StaticRecyclerViewAdapter annexAdapter;

    private RecyclerView boardingRecycler;
    private StaticRecyclerViewAdapter boardingAdapter;
    private ImageView AccountImage;
    private ImageView Drawer;
    private TextView title;
    private EditText SearchField;
    //private CharSequence search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter_and_search_menu_user);

        SearchField = (EditText) findViewById(R.id.SearchInputField);

        SessionManager sessionManager = new SessionManager(FilterAndSearchMenuUser.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        AccountImage = (ImageView) findViewById(R.id.editAccountBtn);
        Drawer = (ImageView) findViewById(R.id.toolBar);
        title = (TextView) findViewById(R.id.TopRatingsTextBoardings);

        PreDataBoardings preDataBoardings = new PreDataBoardings();
        preDataBoardings.execute("");

        PreDataAnnex preDataAnnex = new PreDataAnnex();
        preDataAnnex.execute("");
    }

    @Override
    public void onBackPressed() {

            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
            startActivity(intent);
    }


    public void FilterWindowBoardings(View view){

        Intent intent = new Intent(getApplicationContext(), FilterMenuBoardings.class);

        Pair[] pairs = new Pair[3];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(AccountImage, "userProfile");
        pairs[1] = new Pair<View, String>(Drawer, "drawer");
        pairs[2] = new Pair<View, String>(title, "TitleRating");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FilterAndSearchMenuUser.this, pairs);

        startActivity(intent,options.toBundle());

    }

    public void FilterWindowAnnexes(View view){

        Intent intent = new Intent(getApplicationContext(), FilterMenuAnnexes.class);

        Pair[] pairs = new Pair[3];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(AccountImage, "userProfile");
        pairs[1] = new Pair<View, String>(Drawer, "drawer");
        pairs[2] = new Pair<View, String>(title, "TitleRating");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FilterAndSearchMenuUser.this, pairs);

        startActivity(intent,options.toBundle());

    }

    public void moveToClientEditAccount(View view){


        Intent intent = new Intent(getApplicationContext(), EditAccountWindow.class);

        //intent.putExtra(EXTRA_TEXT, passingID);
        startActivity(intent);
    }

    public class PreDataBoardings extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        String z = "";
        ArrayList<StaticRecyclerViewModel> Boardings = new ArrayList<>();
        boolean isSuccess = false;
        float rating = 0.0f;

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

                        String queryCheck = "select * from AppUser where Username = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSetCheck = statement.executeQuery(queryCheck);

                        if (resultSetCheck.next()) {
                            z = "Login Successful";

                            String queryFetchDataBoarding = "select BoardingHouseRooms.RoomID,BoardingHouseRooms.BoardingID,BoardingHouse.BoardingID,BoardingHouse.City,BoardingHouseRooms.IsOccupied,BoardingHouseRooms.NoOfBeds,BoardingHouseRooms.MaleOrFemale,BoardingHouseRooms.RentalPerPerson from BoardingHouseRooms,BoardingHouse where BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID and IsOccupied = 'No';";
                            ResultSet resultSetFetchBoardings = statement.executeQuery(queryFetchDataBoarding);
                            String gender;

                            String BoardingFor;
                            String RoomID;

                            while (resultSetFetchBoardings.next()){

                                RoomID = resultSetFetchBoardings.getString("RoomID");

                                gender = resultSetFetchBoardings.getString("MaleOrFemale");

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
                                Boardings.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Boarding in " + resultSetFetchBoardings.getString("City"), "Beds : " + resultSetFetchBoardings.getString("NoOfBeds"), BoardingFor, "Rs." + resultSetFetchBoardings.getString("RentalPerPerson"), rating,resultSetFetchBoardings.getString("RoomID"),"0"));


                            }
                            boardingRecycler = findViewById(R.id.RecyclerViewBoardingResults);
                            boardingAdapter = new StaticRecyclerViewAdapter(Boardings,this);
                            boardingRecycler.setLayoutManager(new LinearLayoutManager(FilterAndSearchMenuUser.this, LinearLayoutManager.HORIZONTAL, false));
                            boardingRecycler.setAdapter(boardingAdapter);

                            SearchField.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    boardingAdapter.CancelTimer();

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    if (Boardings.size() != 0){
                                        boardingAdapter.searchAds(s.toString());
                                    }

                                }
                            });


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

            String SelectedItemIDBoarding = Boardings.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(FilterAndSearchMenuUser.this,SessionManager.SESSION_REMEMBERME_BOARDING);
            sessionManager.createRememberMeSessionBoarding(SelectedItemIDBoarding);



            Intent intentToAd = new Intent(getApplicationContext(), AdViewUserBoarding.class);

            //intentToAd.putExtra(EXTRA_TEXT,SelectedItemID);

            startActivity(intentToAd);

        }
    }

    public class PreDataAnnex extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        String z = "";
        ArrayList<StaticRecyclerViewModel> Annexes = new ArrayList<>();
        boolean isSuccess = false;
        float rating = 0.0f;

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

                        String queryCheck = "select * from AppUser where Username = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSetCheck = statement.executeQuery(queryCheck);

                        if (resultSetCheck.next()) {
                            z = "Login Successful";
                            String AnnexID;

                            String query = "select * from Annex where IsOccupied = 'No';";
                            ResultSet resultSet = statement.executeQuery(query);




                            while (resultSet.next()) {

                                AnnexID = resultSet.getString("AnnexID");

                                String queryOverallRating = "select avg(Rating) as Rating,AnnexID from AnnexRating where AnnexID = '" + AnnexID+ "' group by AnnexID;";
                                Statement statementRating = connection.createStatement();
                                ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                                if(resultSetRating.next()){
                                    rating = Float.parseFloat(resultSetRating.getString("Rating"));
                                }

                                Annexes.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Annex in " + resultSet.getString("City"), "Rooms : " + resultSet.getString("NoOfRooms"), "Key Money : Rs." + resultSet.getString("KeyMoney"), "Rs." + resultSet.getString("RentalPerMonth"), rating,resultSet.getString("AnnexID"),"0"));


                            }

                            annexRecycler = findViewById(R.id.RecyclerViewAnnexesResults);
                            annexAdapter = new StaticRecyclerViewAdapter(Annexes,this);
                            annexRecycler.setLayoutManager(new LinearLayoutManager(FilterAndSearchMenuUser.this, LinearLayoutManager.HORIZONTAL, false));
                            annexRecycler.setAdapter(annexAdapter);

                            SearchField.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    annexAdapter.CancelTimer();

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    if (Annexes.size() != 0){
                                        annexAdapter.searchAds(s.toString());
                                    }

                                }
                            });


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

            String SelectedItemID = Annexes.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(FilterAndSearchMenuUser.this,SessionManager.SESSION_REMEMBERME_ANNEX);
            sessionManager.createRememberMeSessionAnnex(SelectedItemID);



            Intent intentToAd = new Intent(getApplicationContext(), AdViewUser.class);

            //intentToAd.putExtra(EXTRA_TEXT,SelectedItemID);

            startActivity(intentToAd);

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