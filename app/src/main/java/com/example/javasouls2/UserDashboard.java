package com.example.javasouls2;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javasouls2.HelperClasses.AnnexHelperClass;
import com.example.javasouls2.HelperClasses.FeaturedAdapter;
import com.example.javasouls2.User.FilterAndSearchMenuUser;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDashboard extends AppCompatActivity implements DatabaseCredentials, NavigationView.OnNavigationItemSelectedListener {

    //Variables
    private ImageView Account;
    private ImageView DrawerButton;
    private RelativeLayout searchBar;
    private LinearLayout mainButtons;

    private String passingID;
    private Connection connection;

    private RecyclerView annexRecycler;
    private StaticRecyclerViewAdapter annexAdapter;

    private RecyclerView boardingRecycler;
    private StaticRecyclerViewAdapter boardingAdapter;

    private RecyclerView adRecyclerView;
    private FeaturedAdapter adapter;
    private TextView welcomeTitle;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private String SelectedItemID;

    public static final String EXTRA_TEXT = "com.example.javasouls2.UserDashboard.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //Hooks
        Account = (ImageView) findViewById(R.id.editAccountBtn);
        DrawerButton = (ImageView) findViewById(R.id.toolBar);
        searchBar = (RelativeLayout) findViewById(R.id.SearchBar);
        mainButtons= (LinearLayout) findViewById(R.id.MainTwoButtons);
        welcomeTitle = (TextView) findViewById(R.id.WelcomeText);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayoutUser);
        navigationView = (NavigationView) findViewById(R.id.nav_view_User);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


        SessionManager sessionManager = new SessionManager(UserDashboard.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        PreDataAnnex preDataAnnex = new PreDataAnnex();
        preDataAnnex.execute("");

        PreDataBoarding preDataBoarding = new PreDataBoarding();
        preDataBoarding.execute("");

    }

    public void OpenDrawer(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void moveToEditAccount(View view) {

        Intent intent = new Intent(getApplicationContext(), EditAccountWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "editAccount");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        //intent.putExtra(EXTRA_TEXT, passingID);
        startActivity(intent, options.toBundle());
    }

    public void MoveToAnnexResults(View view){

        Intent moveToAnxResults = new Intent(getApplicationContext(),AnnexesResult.class);

        Pair[] pairs = new Pair[5];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(DrawerButton, "drawer");
        pairs[2] = new Pair<View, String>(welcomeTitle, "WelcomeText");
        pairs[3] = new Pair<View, String>(searchBar, "Searchbar");
        pairs[4] = new Pair<View, String>(mainButtons, "MainButtons");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(moveToAnxResults, options.toBundle());

    }

    public void SearchAndFilter(View view){

        Intent moveToAnxResults = new Intent(getApplicationContext(), FilterAndSearchMenuUser.class);

        Pair[] pairs = new Pair[3];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(DrawerButton, "drawer");
        pairs[2] = new Pair<View, String>(searchBar, "Searchbar");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(moveToAnxResults, options.toBundle());


    }


    public void MoveToBoardingResults(View view){

        Intent moveToAnxResults = new Intent(getApplicationContext(),BoardingResults.class);

        Pair[] pairs = new Pair[5];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(DrawerButton, "drawer");
        pairs[2] = new Pair<View, String>(welcomeTitle, "WelcomeText");
        pairs[3] = new Pair<View, String>(searchBar, "Searchbar");
        pairs[4] = new Pair<View, String>(mainButtons, "MainButtons");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(moveToAnxResults, options.toBundle());

    }

    /*public void adRecycler() {

        ArrayList<AnnexHelperClass> AnnexLocations = new ArrayList<>();

        AnnexLocations.add(new AnnexHelperClass(R.drawable.annex_ad_1, "Annex In Dalugama", "5", "2"));
        AnnexLocations.add(new AnnexHelperClass(R.drawable.annex_ad_2, "Annex In Kelaniya", "4", "2"));
        AnnexLocations.add(new AnnexHelperClass(R.drawable.annex_ad_3, "Annex In Kiribathgoda", "3", "2"));

        adRecyclerView = findViewById(R.id.adRecycler);
        adRecyclerView.setHasFixedSize(true);

        adapter = new FeaturedAdapter(AnnexLocations);
        adRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        adRecyclerView.setAdapter(adapter);
    }*/

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                break;
            case R.id.nav_account:

                break;
            case R.id.nav_contact:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_logout:
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("rememberME", "false");
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public class PreDataAnnex extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        String z = "";
        boolean isSuccess = false;
        ArrayList<StaticRecyclerViewModel> Annexes = new ArrayList<>();

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



                            String queryFetchData = "select Annex.IsOccupied,Annex.NoOfRooms,Annex.KeyMoney,Annex.RentalPerMonth,Annex.City,Annex.AnnexID,avg(AnnexRating.Rating) as Rating from Annex inner join AnnexRating on Annex.AnnexID = AnnexRating.AnnexID where Annex.IsOccupied = 'No' group by Annex.AnnexID,Annex.City,Annex.NoOfRooms,Annex.KeyMoney,Annex.RentalPerMonth,Annex.IsOccupied having avg(AnnexRating.Rating) > 2 and Annex.IsOccupied = 'No' order by avg(AnnexRating.Rating) desc;";
                            ResultSet resultSetFetchAnnexes = statement.executeQuery(queryFetchData);

                            String AnnexID;

                            float rating;
                            while (resultSetFetchAnnexes.next()) {

                                AnnexID = resultSetFetchAnnexes.getString("AnnexID");

                                rating = Float.parseFloat(resultSetFetchAnnexes.getString("Rating"));
                                Annexes.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Annex in " + resultSetFetchAnnexes.getString("City"), "Rooms : " + resultSetFetchAnnexes.getString("NoOfRooms"), "Key Money : Rs." + resultSetFetchAnnexes.getString("KeyMoney"), "Rs." + resultSetFetchAnnexes.getString("RentalPerMonth"), rating,AnnexID,"0"));

                            }
                            annexRecycler = findViewById(R.id.RecyclerViewAnnexesTopRated);
                            annexAdapter = new StaticRecyclerViewAdapter(Annexes,this);
                            annexRecycler.setLayoutManager(new LinearLayoutManager(UserDashboard.this, LinearLayoutManager.HORIZONTAL, false));
                            annexRecycler.setAdapter(annexAdapter);

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


        @Override
        public void onItemClick(int position) {

            SelectedItemID = Annexes.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(UserDashboard.this,SessionManager.SESSION_REMEMBERME_ANNEX);
            sessionManager.createRememberMeSessionAnnex(SelectedItemID);

            Intent intent = new Intent(getApplicationContext(),AdViewUser.class);

            startActivity(intent);

        }
    }

    public class PreDataBoarding extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        String z = "";
        boolean isSuccess = false;
        ArrayList<StaticRecyclerViewModel> BoardingsTopRated = new ArrayList<>();

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

                            String queryFetchDataBoarding = "select BoardingHouseRooms.IsOccupied,BoardingHouseRooms.RentalPerPerson,BoardingHouseRooms.NoOfBeds,BoardingHouseRooms.MaleOrFemale,BoardingHouse.City,BoardingHouseRooms.RoomID,BoardingHouseRooms.ElectricityBill,avg(BoardingRoomRating.Rating) as Rating from BoardingHouseRooms inner join BoardingRoomRating on BoardingHouseRooms.RoomID = BoardingRoomRating.RoomID inner join BoardingHouse on BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID group by BoardingHouseRooms.RoomID,BoardingHouseRooms.ElectricityBill,BoardingHouse.City,BoardingHouseRooms.MaleOrFemale,BoardingHouseRooms.NoOfBeds,BoardingHouseRooms.RentalPerPerson,BoardingHouseRooms.IsOccupied having avg(BoardingRoomRating.Rating) > 3.5 and BoardingHouseRooms.IsOccupied = 'No' order by avg(BoardingRoomRating.Rating) desc;";
                            ResultSet resultSetFetchBoardings = statement.executeQuery(queryFetchDataBoarding);


                            String RoomID;
                            String gender;
                            String BoardingFor;

                            float rating;
                            while (resultSetFetchBoardings.next()) {

                                RoomID = resultSetFetchBoardings.getString("RoomID");

                                gender = resultSetFetchBoardings.getString("MaleOrFemale");

                                if (gender.equals("Male"))
                                    BoardingFor = "Boarding for boys";
                                else
                                    BoardingFor = "Boarding for girls";

                                rating = Float.parseFloat(resultSetFetchBoardings.getString("Rating"));
                                BoardingsTopRated.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Boarding in " + resultSetFetchBoardings.getString("City"), "Beds : " + resultSetFetchBoardings.getString("NoOfBeds"), BoardingFor, "Rs." + resultSetFetchBoardings.getString("RentalPerPerson"), rating,RoomID,"0"));

                            }
                            boardingRecycler = findViewById(R.id.RecyclerViewBoardingsTopRated);
                            boardingAdapter = new StaticRecyclerViewAdapter(BoardingsTopRated,this);
                            boardingRecycler.setLayoutManager(new LinearLayoutManager(UserDashboard.this, LinearLayoutManager.HORIZONTAL, false));
                            boardingRecycler.setAdapter(boardingAdapter);

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

            String SelectedItemIDBoardings = BoardingsTopRated.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(UserDashboard.this,SessionManager.SESSION_REMEMBERME_BOARDING);
            sessionManager.createRememberMeSessionBoarding(SelectedItemIDBoardings);



            Intent intentToAd = new Intent(getApplicationContext(), AdViewUserBoarding.class);

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