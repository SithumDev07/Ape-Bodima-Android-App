package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.javasouls2.Client.AllBoardings;
import com.example.javasouls2.User.FilterAndSearchMenuUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class FilteredBoardingsWindow extends AppCompatActivity implements DatabaseCredentials{

    String Query;
    private Connection connection;

    private RecyclerView FilteredBoardingsRecycler;
    private StaticRecyclerViewAdapter FilteredBoardingsAdapter;

    private String passingID;
    private String SelectedItemID;

    private EditText Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filtered_boardings_window);

        Search = (EditText) findViewById(R.id.InputSearchBoardings);

        SessionManager sessionManager = new SessionManager(FilteredBoardingsWindow.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        Intent intent = getIntent();
        Query = intent.getStringExtra(FilterMenuBoardings.EXTRA_TEXT);

        GetPreData getPreData = new GetPreData();
        getPreData.execute("");
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), FilterMenuBoardings.class);
        startActivity(intent);
    }

    public void moveToEditAccount(View view){


        Intent intent = new Intent(getApplicationContext(), EditAccountWindow.class);

        //intent.putExtra(EXTRA_TEXT, passingID);
        startActivity(intent);

    }

    public class GetPreData extends AsyncTask<String, String, String> implements RecyclerViewClickInterface {

        String z = "";
        ArrayList<StaticRecyclerViewModel> AllBoardings = new ArrayList<>();
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            if (Query.trim().equals("")) {

                z = "Fatal Error! Passing ID Not Found";

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

                            ResultSet resultSetFetchFilteredBoardings = statement.executeQuery(Query);



                            float rating = 0.0f;
                            String gender;
                            String RoomID;

                            String BoardingFor;
                            while (resultSetFetchFilteredBoardings.next()) {

                                RoomID = resultSetFetchFilteredBoardings.getString("RoomID");

                                gender = resultSetFetchFilteredBoardings.getString("MaleOrFemale");

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
                                AllBoardings.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Boarding in " + resultSetFetchFilteredBoardings.getString("City"), "Beds : " + resultSetFetchFilteredBoardings.getString("NoOfBeds"), BoardingFor, "Rs." + resultSetFetchFilteredBoardings.getString("RentalPerPerson"), rating,RoomID,"0"));

                            }
                            FilteredBoardingsRecycler = findViewById(R.id.RecyclerViewFilteredBoardings);
                            FilteredBoardingsAdapter = new StaticRecyclerViewAdapter(AllBoardings,this);
                            FilteredBoardingsRecycler.setLayoutManager(new LinearLayoutManager(FilteredBoardingsWindow.this, LinearLayoutManager.HORIZONTAL, false));
                            FilteredBoardingsRecycler.setAdapter(FilteredBoardingsAdapter);

                            Search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    FilteredBoardingsAdapter.CancelTimer();

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    if (AllBoardings.size() != 0){
                                        FilteredBoardingsAdapter.searchAds(s.toString());
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

            SelectedItemID = AllBoardings.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(FilteredBoardingsWindow.this,SessionManager.SESSION_REMEMBERME_BOARDING);
            sessionManager.createRememberMeSessionBoarding(SelectedItemID);



            Intent intentToAd = new Intent(getApplicationContext(),AdViewUserBoarding.class);

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