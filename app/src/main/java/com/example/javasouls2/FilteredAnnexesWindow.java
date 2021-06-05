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

import com.example.javasouls2.Client.AllAnnexes;
import com.example.javasouls2.User.FilterAndSearchMenuUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class FilteredAnnexesWindow extends AppCompatActivity implements DatabaseCredentials{

    String Query;
    private Connection connection;

    private RecyclerView FilteredAnnexesRecycler;
    private StaticRecyclerViewAdapter FilteredAnnexesAdapter;

    private String passingID;
    private String SelectedItemID;

    private EditText SearchView;
    //private CharSequence search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filtered_annexes_window);

        SearchView = (EditText) findViewById(R.id.SearchInput);
        Intent intent = getIntent();
        Query = intent.getStringExtra(FilterMenuAnnexes.EXTRA_TEXT);

        SessionManager sessionManager = new SessionManager(FilteredAnnexesWindow.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        GetPreDataAnnexes getPreDataAnnexes = new GetPreDataAnnexes();
        getPreDataAnnexes.execute("");
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), FilterMenuAnnexes.class);
        startActivity(intent);
    }

    public void moveToEditAccount(View view){

        Intent intent = new Intent(getApplicationContext(), EditAccountWindow.class);

        startActivity(intent);

    }

    public class GetPreDataAnnexes extends AsyncTask<String, String, String> implements RecyclerViewClickInterface {

        String z = "";
        ArrayList<StaticRecyclerViewModel> FilteredAnnexes = new ArrayList<>();
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

                            ResultSet resultSetFetchFilteredAnnexes = statement.executeQuery(Query);

                            String AnnexID;
                            float rating = 0.0f;

                            while (resultSetFetchFilteredAnnexes.next()) {

                                AnnexID = resultSetFetchFilteredAnnexes.getString("AnnexID");



                                String queryOverallRating = "select avg(Rating) as Rating,AnnexID from AnnexRating where AnnexID = '" + AnnexID+ "' group by AnnexID;";
                                Statement statementRating = connection.createStatement();
                                ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                                if(resultSetRating.next()){
                                    rating = Float.parseFloat(resultSetRating.getString("Rating"));
                                }

                                FilteredAnnexes.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Annex in " + resultSetFetchFilteredAnnexes.getString("City"), "Rooms : " + resultSetFetchFilteredAnnexes.getString("NoOfRooms"), "Key Money : Rs." + resultSetFetchFilteredAnnexes.getString("KeyMoney"), "Rs." + resultSetFetchFilteredAnnexes.getString("RentalPerMonth"), rating,AnnexID,"0"));

                            }
                            FilteredAnnexesRecycler = findViewById(R.id.RecyclerViewFilteredAnnexes);
                            FilteredAnnexesAdapter = new StaticRecyclerViewAdapter(FilteredAnnexes,this);
                            FilteredAnnexesRecycler.setLayoutManager(new LinearLayoutManager(FilteredAnnexesWindow.this, LinearLayoutManager.HORIZONTAL, false));
                            FilteredAnnexesRecycler.setAdapter(FilteredAnnexesAdapter);

                            SearchView.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    FilteredAnnexesAdapter.CancelTimer();

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    if (FilteredAnnexes.size() != 0){
                                        FilteredAnnexesAdapter.searchAds(s.toString());
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

            SelectedItemID = FilteredAnnexes.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(FilteredAnnexesWindow.this,SessionManager.SESSION_REMEMBERME_ANNEX);
            sessionManager.createRememberMeSessionAnnex(SelectedItemID);



            Intent intentToAd = new Intent(getApplicationContext(),AdViewUser.class);
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