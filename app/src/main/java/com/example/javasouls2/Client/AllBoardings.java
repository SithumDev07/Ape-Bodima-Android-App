package com.example.javasouls2.Client;

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

import com.example.javasouls2.AdEditWindowClinetBoarding;
import com.example.javasouls2.AdViewUserBoarding;
import com.example.javasouls2.AnnexesResult;
import com.example.javasouls2.BoardingResults;
import com.example.javasouls2.ClientPanel;
import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.EditAccountWindow;
import com.example.javasouls2.HelperClasses.AnnexHelperClass;
import com.example.javasouls2.HelperClasses.FeaturedAdapter;
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

public class AllBoardings extends AppCompatActivity implements DatabaseCredentials {

    private ImageView Account;
    private ImageView DrawerButton;
    private TextView MainTitle;

    private String passingID;
    private Connection connection;

    private RecyclerView AllBoardingsRecycler;
    private StaticRecyclerViewAdapter AllBoardingsAdapter;

    private String SelectedItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_boardings);


        Account = (ImageView) findViewById(R.id.editAccountBtn);
        DrawerButton = (ImageView) findViewById(R.id.toolBar);

        SessionManager sessionManager = new SessionManager(AllBoardings.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        GetPreData getPreData = new GetPreData();
        getPreData.execute("");

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), SearchMenu.class);
        startActivity(intent);

    }

    public void MoveToEditAccountUser(View view){

        Toast.makeText(AllBoardings.this, "Moving", Toast.LENGTH_SHORT).show();

        Intent intentToEdit = new Intent(getApplicationContext(), ClientEditAccountWindow.class);


        startActivity(intentToEdit);

    }

    public class GetPreData extends AsyncTask<String, String, String> implements RecyclerViewClickInterface {

        String z = "";
        ArrayList<StaticRecyclerViewModel> AllBoardings = new ArrayList<>();
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
                        String query = "SELECT * FROM BoardingHouse WHERE NIC = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";

                            String queryFetchData = "select BoardingHouseRooms.RoomID,BoardingHouse.NIC,BoardingHouse.City,BoardingHouseRooms.IsOccupied,BoardingHouseRooms.NoOfBeds,BoardingHouseRooms.MaleOrFemale,BoardingHouseRooms.RentalPerPerson from BoardingHouseRooms,BoardingHouse where BoardingHouse.NIC = '" + passingID + "' and BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID;";
                            ResultSet resultSetFetchAnnexes = statement.executeQuery(queryFetchData);



                            float rating;
                            String gender;
                            String RoomID;
                            String BoardingFor;
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
                                else{
                                    rating = 0.0f;
                                }


                                AllBoardings.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Boarding in " + resultSetFetchAnnexes.getString("City"), "Beds : " + resultSetFetchAnnexes.getString("NoOfBeds"), BoardingFor, "Rs." + resultSetFetchAnnexes.getString("RentalPerPerson"), rating,RoomID,"0"));

                            }
                            AllBoardingsRecycler = findViewById(R.id.RecyclerViewAllBoardings);
                            AllBoardingsAdapter = new StaticRecyclerViewAdapter(AllBoardings,this);
                            AllBoardingsRecycler.setLayoutManager(new LinearLayoutManager(AllBoardings.this, LinearLayoutManager.HORIZONTAL, false));
                            AllBoardingsRecycler.setAdapter(AllBoardingsAdapter);

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

            SessionManager sessionManager = new SessionManager(AllBoardings.this,SessionManager.SESSION_REMEMBERME_BOARDING);
            sessionManager.createRememberMeSessionBoarding(SelectedItemID);



            Intent intentToAd = new Intent(getApplicationContext(), AdEditWindowClinetBoarding.class);

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