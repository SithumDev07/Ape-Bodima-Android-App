package com.example.javasouls2.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.EditAccountWindow;
import com.example.javasouls2.OccupyPeopleAnnexPanel;
import com.example.javasouls2.R;
import com.example.javasouls2.RecyclerViewClickInterface;
import com.example.javasouls2.RecyclerViewModelOccupier;
import com.example.javasouls2.SearchMenu;
import com.example.javasouls2.SessionManager;
import com.example.javasouls2.StaticRecyclerViewAdapter;
import com.example.javasouls2.StaticRecyclerViewModel;
import com.example.javasouls2.StaticViewAdapterOccupier;
import com.example.javasouls2.UserDashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class OccupyPeoplePanel extends AppCompatActivity implements DatabaseCredentials {

    private ImageView Account;
    private ImageView drawer;
    private TextView MainTitle;
    private Button OccupyAnnex;
    private Button ShowBr;
    private String passingID;
    private Connection connection;


    private RecyclerView annexUserRecycler;
    private StaticViewAdapterOccupier annexNonUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_occupy_people_panel);

        Account = (ImageView) findViewById(R.id.editAccountBtn);
        drawer = (ImageView) findViewById(R.id.toolBar);
        MainTitle = (TextView) findViewById(R.id.WelcomeText);
        OccupyAnnex = (Button) findViewById(R.id.MoveOccupyAnnex);
        ShowBr = (Button) findViewById(R.id.ShowBoardingsOccupy);


        SessionManager sessionManager = new SessionManager(OccupyPeoplePanel.this, SessionManager.SESSION_REMEMBERME);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        PreDataAlreadyUserAnnex preDataAnnex = new PreDataAlreadyUserAnnex();
        preDataAnnex.execute("");

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), SearchMenu.class);
        startActivity(intent);

    }

    public void MoveToEditAccountUser(View view){

        Toast.makeText(OccupyPeoplePanel.this, "Moving", Toast.LENGTH_SHORT).show();

        Intent intentToEdit = new Intent(getApplicationContext(), EditAccountWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeoplePanel.this, pairs);
        startActivity(intentToEdit, options.toBundle());

    }

    public class PreDataAlreadyUserAnnex extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        String z = "";
        boolean isSuccess = false;
        ArrayList<RecyclerViewModelOccupier> OccupiersInAnnexes = new ArrayList<>();

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

                        String queryCheck = "select * from Annex where NIC = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSetCheck = statement.executeQuery(queryCheck);

                        if (resultSetCheck.next()) {
                            z = "Login Successful";

                            String QueryAlreadyUser = "select Annex.AnnexID,UserAnnex.Username,UserAnnex.AnnexID,AppUser.FirstName,AppUser.PhoneNumber,AppUser.LastName,UserAnnex.OccupiedDate,AppUser.City from Annex,UserAnnex,AppUser where Annex.AnnexID = UserAnnex.AnnexID and Annex.NIC = '" + passingID + "' and AppUser.Username = UserAnnex.Username;";
                            ResultSet resultSet1 = statement.executeQuery(QueryAlreadyUser);

                            while (resultSet1.next()){

                                OccupiersInAnnexes.add(new RecyclerViewModelOccupier(R.drawable.profile_login_activity, resultSet1.getString("FirstName"), "From " + resultSet1.getString("City"), resultSet1.getString("PhoneNumber"), "Since " + resultSet1.getString("OccupiedDate"),resultSet1.getString("Username"),resultSet1.getString("AnnexID")));

                            }

                            String query = "select NonUserOccupierAnnex.OccupierID,Annex.AnnexID,NonUserOccupierAnnex.AnnexID,NonUserOccupierAnnex.FirstName,NonUserOccupierAnnex.PhoneNumber,NonUserOccupierAnnex.City,NonUserOccupierAnnex.OccupiedDate from Annex,NonUserOccupierAnnex where Annex.AnnexID = NonUserOccupierAnnex.AnnexID and Annex.NIC = '" + passingID + "';";
                            ResultSet resultSet = statement.executeQuery(query);

                            while (resultSet.next()) {

                                OccupiersInAnnexes.add(new RecyclerViewModelOccupier(R.drawable.profile_login_activity, resultSet.getString("FirstName"), "From " + resultSet.getString("City"), resultSet.getString("PhoneNumber"), "Since " + resultSet.getString("OccupiedDate"),resultSet.getString("OccupierID"),resultSet.getString("AnnexID")));

                            }

                            annexUserRecycler = findViewById(R.id.RecyclerViewWhoInAnnexUser);
                            annexNonUserAdapter = new StaticViewAdapterOccupier(OccupiersInAnnexes,this);
                            annexUserRecycler.setLayoutManager(new LinearLayoutManager(OccupyPeoplePanel.this, LinearLayoutManager.HORIZONTAL, false));
                            annexUserRecycler.setAdapter(annexNonUserAdapter);

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

            String SelectItemID = OccupiersInAnnexes.get(position).getOccupierID();
            String SelectedItemPlaceID = OccupiersInAnnexes.get(position).getPlaceID();

            AlertDialog dialog = new AlertDialog.Builder(OccupyPeoplePanel.this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure want to remove " + OccupiersInAnnexes.get(position).getName() + " from your annex?")
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

                            String CheckPointID = SelectItemID.substring(0,3);

                            if(CheckPointID.equals("OID")){

                                String queryOccupier = "delete from NonUserOccupierAnnex where OccupierID = '" + SelectItemID + "';";
                                Statement statement = connection.createStatement();
                                statement.executeUpdate(queryOccupier);

                                String queryReStateOccupy = "update Annex set IsOccupied = 'No' where AnnexID = '" + SelectedItemPlaceID + "';";
                                statement.executeUpdate(queryReStateOccupy);

                            }
                            else{

                                String queryOccupier = "delete from UserAnnex where Username = '" + SelectItemID + "';";
                                Statement statement = connection.createStatement();
                                statement.executeUpdate(queryOccupier);

                                String queryReStateOccupy = "update Annex set IsOccupied = 'No' where AnnexID = '" + SelectedItemPlaceID + "';";
                                statement.executeUpdate(queryReStateOccupy);

                            }



                        }

                        connection.close();
                    } catch (Exception e) {

                    }

                    Toast.makeText(OccupyPeoplePanel.this, "Removed", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), OccupyPeoplePanel.class);

                    startActivity(intent);
                }
            });
        }
    }



    public void ShowBoardings(View view){

        Intent intentToOccupyAnnex = new Intent(getApplicationContext(), OccupyPeoplePanel2.class);

        Pair[] pairs = new Pair[5];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(drawer, "drawer");
        pairs[2] = new Pair<View, String>(MainTitle, "mainTitle");
        pairs[3] = new Pair<View, String>(OccupyAnnex, "OccupyAnnex");
        pairs[4] = new Pair<View, String>(ShowBr, "transBtn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeoplePanel.this, pairs);
        startActivity(intentToOccupyAnnex,options.toBundle());

    }



    public void MoveToOccupyPeopleAnnex(View view){
        Toast.makeText(OccupyPeoplePanel.this, "Moving to Annex", Toast.LENGTH_SHORT).show();

        Intent intentToOccupyAnnex = new Intent(getApplicationContext(), OccupyPeopleAnnexPanel.class);

        Pair[] pairs = new Pair[4];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(drawer, "drawer");
        pairs[2] = new Pair<View, String>(MainTitle, "mainTitle");
        pairs[3] = new Pair<View, String>(OccupyAnnex, "OccupyAnnex");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeoplePanel.this, pairs);
        startActivity(intentToOccupyAnnex);
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