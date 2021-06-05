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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.EditAccountWindow;
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

public class OccupyPeopleAlreadyUserBoarding extends AppCompatActivity implements DatabaseCredentials {

    private ImageView Account;
    private ImageView drawer;
    private TextView MainTitle;
    private Button Occupy;
    private DatePicker OccupyDate;
    private String passingID;
    private String SelectedBoardingID;
    private String SelectedRoomID;
    private Connection connection;

    private EditText Username;

    private RecyclerView AllBoardingsForOccupierRecycler;
    private StaticRecyclerViewAdapter AllBoardingsOccupyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_occupy_people_already_user_boarding);

        Username = (EditText) findViewById(R.id.NameInputUser);
        OccupyDate = (DatePicker) findViewById(R.id.DatePickerSpinner);

        SessionManager sessionManager = new SessionManager(OccupyPeopleAlreadyUserBoarding.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        GetPreData getPreData = new GetPreData();
        getPreData.execute("");

    }

    public void UpdateDataOC(View view) throws SQLException {


        DataValidationOccupyUserAnnex dataValidationOccupyUserAnnex = new DataValidationOccupyUserAnnex(Username);

        if(dataValidationOccupyUserAnnex.GetValidationUsername()){

            SendData sendData = new SendData();
            sendData.execute("");

        }
        else{
            return;
        }
    }

    public void MoveToEditAccountUser(View view){

        Toast.makeText(OccupyPeopleAlreadyUserBoarding.this, "Moving", Toast.LENGTH_SHORT).show();

        Intent intentToEdit = new Intent(getApplicationContext(), EditAccountWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeopleAlreadyUserBoarding.this, pairs);
        startActivity(intentToEdit, options.toBundle());

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), OccupyPeopleAddWindow.class);
        startActivity(intent);

    }

    public class GetPreData extends AsyncTask<String, String, String> implements RecyclerViewClickInterface {

        String z = "";
        boolean isSuccess = false;
        ArrayList<StaticRecyclerViewModel> AllBoardingsForOccupy = new ArrayList<>();

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

                            String queryFetchData = "select BoardingHouseRooms.RoomID,BoardingHouse.BoardingID,BoardingHouse.NIC,BoardingHouse.City,BoardingHouseRooms.IsOccupied,BoardingHouseRooms.NoOfBeds,BoardingHouseRooms.MaleOrFemale,BoardingHouseRooms.RentalPerPerson from BoardingHouseRooms,BoardingHouse where BoardingHouse.NIC = '" + passingID + "' and BoardingHouse.BoardingID = BoardingHouseRooms.BoardingID;";
                            ResultSet resultSetFetchAnnexes = statement.executeQuery(queryFetchData);


                            String BoardingID;
                            float rating = 0.0f;
                            String RoomID;
                            String gender;
                            String BoardingFor;
                            while (resultSetFetchAnnexes.next()) {

                                RoomID = resultSetFetchAnnexes.getString("RoomID");

                                gender = resultSetFetchAnnexes.getString("MaleOrFemale");
                                BoardingID = resultSetFetchAnnexes.getString("BoardingID");

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

                                AllBoardingsForOccupy.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Boarding in " + resultSetFetchAnnexes.getString("City"), "Beds : " + resultSetFetchAnnexes.getString("NoOfBeds"), BoardingFor, "Rs." + resultSetFetchAnnexes.getString("RentalPerPerson"), rating,BoardingID,RoomID));

                            }
                            AllBoardingsForOccupierRecycler = findViewById(R.id.RecyclerViewSelectBoardingAlready);
                            AllBoardingsOccupyAdapter = new StaticRecyclerViewAdapter(AllBoardingsForOccupy,this);
                            AllBoardingsForOccupierRecycler.setLayoutManager(new LinearLayoutManager(OccupyPeopleAlreadyUserBoarding.this, LinearLayoutManager.HORIZONTAL, false));
                            AllBoardingsForOccupierRecycler.setAdapter(AllBoardingsOccupyAdapter);

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

            SelectedBoardingID = AllBoardingsForOccupy.get(position).getBoardingID();
            SelectedRoomID = AllBoardingsForOccupy.get(position).getRoomID();
        }
    }

    public class SendData extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(OccupyPeopleAlreadyUserBoarding.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(OccupyPeopleAlreadyUserBoarding.this , "All Done" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), OccupyPeoplePanel.class);

                //Animation

                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String UserOC = Username.getText().toString();

            String Day = Integer.toString(OccupyDate.getDayOfMonth());
            String Month = Integer.toString(OccupyDate.getMonth());
            String Year = Integer.toString(OccupyDate.getYear());

            if(UserOC.equals("")){
                z = "Passwords are not match";
            }
            else{

                if(passingID.trim().equals("")){

                    z = "Please enter valid username and password";

                }
                else{
                    try {
                        connection = connectionclass(User,Pass,DB,IP,PORT);

                        if(connection == null){
                            z = "turn on cellular data or Wi-Fi";
                        }
                        else{

                            Statement statement = connection.createStatement();
                            String queryUpdate = "insert into UserBoardingHouseRooms(Username,RoomID,OccupiedDate) values ('" + UserOC + "','" + SelectedRoomID + "','" + Year + "-" + Month + "-" + Day + "');";
                            statement.executeUpdate(queryUpdate);

                            String queryRating = "insert into BoardingRoomRating(Username,RoomID,Rating) values ('" + UserOC + "','" + SelectedRoomID + "',0.0);";
                            statement.executeUpdate(queryRating);

                        }
                        isSuccess = true;
                        connection.close();
                    }
                    catch (Exception e){
                        isSuccess = false;
                        z = e.getMessage();
                    }
                }

            }

            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user,String password, String database, String server, String port){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //ConnectionURL = "jdbc:jtds:sqlserver://" + server + ":" + port + "/" + database;
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