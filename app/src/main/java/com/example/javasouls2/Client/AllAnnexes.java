package com.example.javasouls2.Client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.R;
import com.example.javasouls2.RecyclerViewClickInterface;
import com.example.javasouls2.SearchMenu;
import com.example.javasouls2.SessionManager;
import com.example.javasouls2.StaticRecyclerViewAdapter;
import com.example.javasouls2.StaticRecyclerViewModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class AllAnnexes extends AppCompatActivity implements DatabaseCredentials {

    private ImageView Account;
    private ImageView DrawerButton;
    private TextView MainTitle;

    private String passingID;
    private Connection connection;

    private RecyclerView AllAnnexesRecycler;
    private StaticRecyclerViewAdapter AllAnnexesAdapter;
    String SelectedItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_annexes);

        Account = (ImageView) findViewById(R.id.editAccountBtn);
        DrawerButton = (ImageView) findViewById(R.id.toolBar);

        SessionManager sessionManager = new SessionManager(AllAnnexes.this, SessionManager.SESSION_REMEMBERME);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        GetPreData getPreData = new GetPreData();
        getPreData.execute("");
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), SearchMenu.class);
        startActivity(intent);

    }

    public void MoveToEditAccountUser(View view) {

        Toast.makeText(AllAnnexes.this, "Moving", Toast.LENGTH_SHORT).show();

        Intent intentToEdit = new Intent(getApplicationContext(), ClientEditAccountWindow.class);


        startActivity(intentToEdit);

    }

    public class GetPreData extends AsyncTask<String, String, String> implements RecyclerViewClickInterface {

        ArrayList<StaticRecyclerViewModel> AllAnnexes = new ArrayList<>();
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            if (passingID.trim().equals("")) {

                z = "Fatal Error! Passing ID Not Found";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {

                        String query = "SELECT * FROM Annex WHERE NIC = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";

                            String queryFetchData = "SELECT * FROM Annex WHERE NIC = '" + passingID + "';";
                            ResultSet resultSetFetchAnnexes = statement.executeQuery(queryFetchData);


                            String AnnexID;
                            float rating;
                            while (resultSetFetchAnnexes.next()) {

                                AnnexID = resultSetFetchAnnexes.getString("AnnexID");


                                String queryOverallRating = "select avg(Rating) as Rating,AnnexID from AnnexRating where AnnexID = '" + AnnexID + "' group by AnnexID;";
                                Statement statementRating = connection.createStatement();
                                ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                                if (resultSetRating.next()) {
                                    rating = Float.parseFloat(resultSetRating.getString("Rating"));
                                } else {
                                    rating = 0.0f;
                                }

                                AllAnnexes.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Annex in " + resultSetFetchAnnexes.getString("City"), "Rooms : " + resultSetFetchAnnexes.getString("NoOfRooms"), "Key Money : Rs." + resultSetFetchAnnexes.getString("KeyMoney"), "Rs." + resultSetFetchAnnexes.getString("RentalPerMonth"), rating, AnnexID,"0"));

                            }

                            AllAnnexesRecycler = findViewById(R.id.RecyclerViewAllAnnexes);
                            AllAnnexesAdapter = new StaticRecyclerViewAdapter(AllAnnexes, this);
                            AllAnnexesRecycler.setLayoutManager(new LinearLayoutManager(AllAnnexes.this, LinearLayoutManager.HORIZONTAL, false));
                            AllAnnexesRecycler.setAdapter(AllAnnexesAdapter);

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

            SelectedItemID = AllAnnexes.get(position).getBoardingID();

            SessionManager sessionManager = new SessionManager(AllAnnexes.this, SessionManager.SESSION_REMEMBERME_ANNEX);
            sessionManager.createRememberMeSessionAnnex(SelectedItemID);


            Intent intentToAd = new Intent(getApplicationContext(), AdEditWindowClient.class);


            startActivity(intentToAd);

        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server, String port) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");


            //ConnectionURL = "jdbc:jtds:sqlserver://apebodima.database.windows.net:1433;DatabaseName=ApeBodima;user=adminApeBodima@apebodima;password=Fr13nds2552;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";


            con = DriverManager.getConnection(ConnectionURLS, UserD, PassD);

        } catch (SQLException se) {
            Log.e("Error here 1: ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Error here 2: ", e.getMessage());
        } catch (Exception e) {
            Log.e("Error here 3: ", e.getMessage());
        }
        return con;
    }
}