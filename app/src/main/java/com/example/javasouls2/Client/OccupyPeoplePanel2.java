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
import com.example.javasouls2.R;
import com.example.javasouls2.RecyclerViewClickInterface;
import com.example.javasouls2.RecyclerViewModelOccupier;
import com.example.javasouls2.SearchMenu;
import com.example.javasouls2.SessionManager;
import com.example.javasouls2.StaticViewAdapterOccupier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class OccupyPeoplePanel2 extends AppCompatActivity implements DatabaseCredentials {

    private ImageView Account;
    private ImageView drawer;
    private TextView MainTitle;
    private Button Occupy;
    private Button ShowAnx;
    private String passingID;
    private Connection connection;

    private RecyclerView boardingUserRecycler;
    private StaticViewAdapterOccupier boardingUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_occupy_people_panel2);

        Account = (ImageView) findViewById(R.id.editAccountBtn);
        drawer = (ImageView) findViewById(R.id.toolBarBr);
        MainTitle = (TextView) findViewById(R.id.WelcomeText);
        Occupy = (Button)findViewById(R.id.MoveOccupyBoardings);
        ShowAnx = (Button) findViewById(R.id.ShowAnnexesOccupy);

        SessionManager sessionManager = new SessionManager(OccupyPeoplePanel2.this, SessionManager.SESSION_REMEMBERME);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);


        PreDataAlreadyUserBoarding preDataAlreadyUserBoarding = new PreDataAlreadyUserBoarding();
        preDataAlreadyUserBoarding.execute("");
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), OccupyPeoplePanel.class);
        startActivity(intent);

    }

    public void MoveToEditAccountUser(View view){

        Toast.makeText(OccupyPeoplePanel2.this, "Moving", Toast.LENGTH_SHORT).show();

        Intent intentToEdit = new Intent(getApplicationContext(), EditAccountWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeoplePanel2.this, pairs);
        startActivity(intentToEdit, options.toBundle());

    }

    public void ShowAnnexes(View view){
        Intent intentToOccupyAnnex = new Intent(getApplicationContext(), OccupyPeoplePanel.class);

        Pair[] pairs = new Pair[5];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(drawer, "drawer");
        pairs[2] = new Pair<View, String>(MainTitle, "mainTitle");
        pairs[3] = new Pair<View, String>(Occupy, "OccupyAnnex");
        pairs[4] = new Pair<View, String>(ShowAnx, "transBtn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeoplePanel2.this, pairs);
        startActivity(intentToOccupyAnnex,options.toBundle());
    }

    public void MoveToOccupyPeople(View view){
        Toast.makeText(OccupyPeoplePanel2.this, "Moving", Toast.LENGTH_SHORT).show();

        Intent intentToOccupy = new Intent(getApplicationContext(), OccupyPeopleAddWindow.class);

        Pair[] pairs = new Pair[4];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(drawer, "drawer");
        pairs[2] = new Pair<View, String>(MainTitle, "mainTitle");
        pairs[3] = new Pair<View, String>(Occupy, "OccupyAnnex");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeoplePanel2.this, pairs);
        startActivity(intentToOccupy, options.toBundle());
    }



    public class PreDataAlreadyUserBoarding extends AsyncTask<String, String, String> implements RecyclerViewClickInterface {

        String z = "";
        boolean isSuccess = false;
        ArrayList<RecyclerViewModelOccupier> OccupierBoardingsAlreadyUser = new ArrayList<>();

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

                        String queryCheck = "select * from BoardingHouse where NIC = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSetCheck = statement.executeQuery(queryCheck);
                        String City = "";

                        if (resultSetCheck.next()) {
                            z = "Login Successful";


                            String queryFetchDataBoarding = "select AppUser.Username,UserBoardingHouseRooms.Username,AppUser.FirstName,AppUser.LastName,AppUser.PhoneNumber,UserBoardingHouseRooms.OccupiedDate,UserBoardingHouseRooms.RoomID,BoardingHouse.NIC,AppUser.City from AppUser,UserBoardingHouseRooms,BoardingHouse,BoardingHouseRooms where BoardingHouse.NIC = '" + passingID + "' and BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID and BoardingHouseRooms.RoomID = UserBoardingHouseRooms.RoomID and AppUser.Username = UserBoardingHouseRooms.Username;";
                            ResultSet resultSetFetchBoardings = statement.executeQuery(queryFetchDataBoarding);



                            while (resultSetFetchBoardings.next()) {

                                if (resultSetFetchBoardings.getString("City").length() > 10){
                                    City = resultSetFetchBoardings.getString("City").substring(0,10);
                                }

                                OccupierBoardingsAlreadyUser.add(new RecyclerViewModelOccupier(R.drawable.profile_login_activity, resultSetFetchBoardings.getString("FirstName"), "From " + City, resultSetFetchBoardings.getString("PhoneNumber"), "Since " + resultSetFetchBoardings.getString("OccupiedDate"),resultSetFetchBoardings.getString("Username"),resultSetFetchBoardings.getString("RoomID")));

                            }

                            String QueryAlreadyUserBoarding = "select distinct NonUserOccupierBoarding.OccupierID,BoardingHouse.NIC,BoardingHouseRooms.RoomID,NonUserOccupierBoarding.RoomID,NonUserOccupierBoarding.FirstName,NonUserOccupierBoarding.PhoneNumber,NonUserOccupierBoarding.City,NonUserOccupierBoarding.OccupiedDate from BoardingHouse,BoardingHouseRooms,NonUserOccupierBoarding where BoardingHouseRooms.RoomID = NonUserOccupierBoarding.RoomID and BoardingHouse.NIC = '" + passingID + "';";
                            ResultSet resultSet2 = statement.executeQuery(QueryAlreadyUserBoarding);


                            while (resultSet2.next()){

                                if (resultSet2.getString("City").length() > 10){
                                    City = resultSet2.getString("City").substring(0,10);
                                }

                                OccupierBoardingsAlreadyUser.add(new RecyclerViewModelOccupier(R.drawable.profile_login_activity, resultSet2.getString("FirstName"), "From " + City, resultSet2.getString("PhoneNumber"), "Since " + resultSet2.getString("OccupiedDate"),resultSet2.getString("OccupierID"),resultSet2.getString("RoomID")));

                            }

                            boardingUserRecycler = findViewById(R.id.RecyclerViewWhoInBoardingsUser);
                            boardingUserAdapter = new StaticViewAdapterOccupier(OccupierBoardingsAlreadyUser,this);
                            boardingUserRecycler.setLayoutManager(new LinearLayoutManager(OccupyPeoplePanel2.this, LinearLayoutManager.HORIZONTAL, false));
                            boardingUserRecycler.setAdapter(boardingUserAdapter);

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

            String SelectItemID = OccupierBoardingsAlreadyUser.get(position).getOccupierID();
            String SelectedItemPlaceID = OccupierBoardingsAlreadyUser.get(position).getPlaceID();

            AlertDialog dialog = new AlertDialog.Builder(OccupyPeoplePanel2.this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure want to remove " + OccupierBoardingsAlreadyUser.get(position).getName() + " from your boarding?")
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

                                String queryOccupier = "delete from NonUserOccupierBoarding where OccupierID = '" + SelectItemID + "';";
                                Statement statement = connection.createStatement();
                                statement.executeUpdate(queryOccupier);

                                String queryReStateOccupy = "update BoardingHouseRooms set IsOccupied = 'No' where RoomID = '" + SelectedItemPlaceID + "';";
                                statement.executeUpdate(queryReStateOccupy);

                            }
                            else{

                                String queryOccupier = "delete from UserBoardingHouseRooms where Username = '" + SelectItemID + "';";
                                Statement statement = connection.createStatement();
                                statement.executeUpdate(queryOccupier);

                                String queryReStateOccupy = "update BoardingHouseRooms set IsOccupied = 'No' where RoomID = '" + SelectedItemPlaceID + "';";
                                statement.executeUpdate(queryReStateOccupy);

                            }



                        }

                        connection.close();
                    } catch (Exception e) {

                    }

                    Toast.makeText(OccupyPeoplePanel2.this, "Removed", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), OccupyPeoplePanel2.class);

                    startActivity(intent);
                }
            });
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