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

public class AnnexesResult extends AppCompatActivity implements DatabaseCredentials{

    private ImageView Account;
    private ImageView DrawerButton;
    private TextView welcomeTitle;
    private RelativeLayout searchBar;
    private LinearLayout mainButtons;

    private String passingID;
    private Connection connection;

    private RecyclerView annexRecycler;
    private StaticRecyclerViewAdapter annexAdapter;

    private String SelectedItemID;

    public static final String EXTRA_TEXT = "com.example.javasouls2.AnnexesResult.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_annexes_result);

        Account = (ImageView) findViewById(R.id.editAccountBtn);
        DrawerButton = (ImageView) findViewById(R.id.toolBar);
        searchBar = (RelativeLayout) findViewById(R.id.SearchBarResultsAnnex);
        mainButtons= (LinearLayout) findViewById(R.id.MainTwoButtons);
        welcomeTitle = (TextView) findViewById(R.id.WelcomeText);

        SessionManager sessionManager = new SessionManager(AnnexesResult.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        GetPreData getPreData = new GetPreData();
        getPreData.execute("");
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

    public void MoveToBoardingResults(View view){

        Intent moveToAnxResults = new Intent(getApplicationContext(),BoardingResults.class);
        startActivity(moveToAnxResults);

    }

    public class GetPreData extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        ArrayList<StaticRecyclerViewModel> AnnexesResults = new ArrayList<>();
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

                            String queryFetchData = "SELECT * FROM Annex WHERE IsOccupied = 'No';";
                            ResultSet resultSetFetchAnnexes = statement.executeQuery(queryFetchData);

                            String AnnexID;
                            float rating = 0.0f;
                            while (resultSetFetchAnnexes.next()) {

                                AnnexID = resultSetFetchAnnexes.getString("AnnexID");

                                String queryOverallRating = "select avg(Rating) as Rating,AnnexID from AnnexRating where AnnexID = '" + AnnexID+ "' group by AnnexID;";
                                Statement statementRating = connection.createStatement();
                                ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                                if(resultSetRating.next()){
                                    rating = Float.parseFloat(resultSetRating.getString("Rating"));
                                }

                                AnnexesResults.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Annex in " + resultSetFetchAnnexes.getString("City"), "Rooms : " + resultSetFetchAnnexes.getString("NoOfRooms"), "Key Money : Rs." + resultSetFetchAnnexes.getString("KeyMoney"), "Rs." + resultSetFetchAnnexes.getString("RentalPerMonth"), rating,resultSetFetchAnnexes.getString("AnnexID"),"0"));

                            }
                            annexRecycler = findViewById(R.id.RecyclerView2);
                            annexAdapter = new StaticRecyclerViewAdapter(AnnexesResults,this);
                            annexRecycler.setLayoutManager(new LinearLayoutManager(AnnexesResult.this, LinearLayoutManager.HORIZONTAL, false));
                            annexRecycler.setAdapter(annexAdapter);

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
            SelectedItemID = AnnexesResults.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(AnnexesResult.this,SessionManager.SESSION_REMEMBERME_ANNEX);
            sessionManager.createRememberMeSessionAnnex(SelectedItemID);



            Intent intentToAd = new Intent(getApplicationContext(),AdViewUser.class);

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

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AnnexesResult.this, pairs);
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