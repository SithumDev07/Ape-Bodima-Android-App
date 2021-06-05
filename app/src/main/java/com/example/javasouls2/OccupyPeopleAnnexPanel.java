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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasouls2.Client.OccupyPeopleAddWindow;
import com.example.javasouls2.Client.OccupyPeopleAlreadyUserAnnex;
import com.example.javasouls2.Client.OccupyPeopleAlreadyUserBoarding;
import com.example.javasouls2.Client.OccupyPeoplePanel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class OccupyPeopleAnnexPanel extends AppCompatActivity implements DatabaseCredentials{

    private ImageView Account;
    private ImageView drawer;
    private TextView MainTitle;
    private Button Occupy;
    private Button AlreadyUser;

    private DatePicker OccupyDate;
    private String passingID;
    private String SelectedAnnexID;
    private String OccupierID;
    private Connection connection;

    private EditText FirstName;
    private EditText LastName;
    private EditText PhoneNumber;
    private EditText GuardianPhone;
    private EditText AddressLine1;
    private EditText AddressLine2;
    private EditText City;
    private RadioButton Male;
    private String GenderV;
    private RadioButton Female;

    private RecyclerView AllAnnexesForOccupierRecycler;
    private StaticRecyclerViewAdapter AllAnnexesOccupyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_occupy_people_annex_panel);

        //drawer = (ImageView) findViewById(R.id.toolBar);
       // MainTitle = (TextView) findViewById(R.id.WelcomeText);
        //AlreadyUser = (Button) findViewById(R.id.AlreadyUserBtn);

        Account = (ImageView) findViewById(R.id.editAccountBtnAnnexOccupier);
        FirstName = (EditText) findViewById(R.id.NameInputFirstAnnex);
        LastName = (EditText) findViewById(R.id.NameInputLastAnnexOccupier);
        PhoneNumber = (EditText) findViewById(R.id.NameInputPhoneAnnex);
        GuardianPhone = (EditText) findViewById(R.id.NameInputGuardianPhoneAnnex);
        AddressLine1 = (EditText) findViewById(R.id.AddressLine1InputAnnex);
        AddressLine2 = (EditText) findViewById(R.id.AddressLine2InputAnnex);
        City = (EditText) findViewById(R.id.CityInputAnnex);
        Male = (RadioButton) findViewById(R.id.MaleCheckedAnnex);
        Female = (RadioButton) findViewById(R.id.FemaleCheckedAnnex);

        GenderV = "O";

        OccupyDate = (DatePicker) findViewById(R.id.DatePickerSpinnerAnnex);


        SessionManager sessionManager = new SessionManager(OccupyPeopleAnnexPanel.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        Male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    GenderV = "M";
                }
            }
        });

        Female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    GenderV = "F";
                }
            }
        });

        GetOccupierID getOccupierID = new GetOccupierID();
        getOccupierID.execute("");

        GetPreData getPreData = new GetPreData();
        getPreData.execute("");
    }


    public void UpdateDataClicked(View view){

        SendData sendData = new SendData();
        sendData.execute("");

    }

    public void MoveToAlreadyUser(View view){

        Toast.makeText(OccupyPeopleAnnexPanel.this, "Already User", Toast.LENGTH_SHORT).show();

        Intent intentToAlreadyUser = new Intent(getApplicationContext(), OccupyPeopleAlreadyUserAnnex.class);

        /*Pair[] pairs = new Pair[7];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");
        pairs[1] = new Pair<View, String>(drawer, "drawer");
        pairs[2] = new Pair<View, String>(MainTitle, "occupyTitle");
        pairs[3] = new Pair<View, String>(AlreadyUser, "Already");
        pairs[4] = new Pair<View, String>(FirstName, "username_");
        pairs[5] = new Pair<View, String>(OccupyDate, "DatePicker");
        pairs[6] = new Pair<View, String>(recyclerAnim, "Recycler");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeopleAnnexPanel.this, pairs);*/
        startActivity(intentToAlreadyUser);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), OccupyPeoplePanel.class);
        startActivity(intent);

    }

    public void MoveToEditAccountUser(View view){

        Toast.makeText(OccupyPeopleAnnexPanel.this, "Moving", Toast.LENGTH_SHORT).show();

        Intent intentToEdit = new Intent(getApplicationContext(), EditAccountWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View, String>(Account, "userProfile");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OccupyPeopleAnnexPanel.this, pairs);
        startActivity(intentToEdit, options.toBundle());

    }

    public String getOccupierID(String ID){
        int lastDigits = Integer.parseInt(ID.substring(3,8));
        lastDigits++;

        if(lastDigits <= 9) ID = "OID0000" + Integer.toString(lastDigits);

        else if(lastDigits > 9 && lastDigits <= 99) ID = "OID000" + Integer.toString(lastDigits);

        else if(lastDigits > 99 && lastDigits <= 999) ID = "OID00" + Integer.toString(lastDigits);

        else if(lastDigits > 999 && lastDigits <= 9999) ID = "OID0" + Integer.toString(lastDigits);

        else ID = "OID" + Integer.toString(lastDigits);

        return ID;
    }

    public class GetOccupierID extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            if (passingID.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT TOP 1 * FROM NonUserOccupierAnnex ORDER BY OccupierID desc;";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";

                            OccupierID = resultSet.getString("OccupierID");
                            OccupierID = getOccupierID(OccupierID);
                            //BID00000

                            isSuccess = true;
                            connection.close();
                        } else {
                            OccupierID = "OID00000";
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
        protected void onPostExecute(String r)
        {
        }

        @Override
        protected String doInBackground(String... params) {

            return z;
        }
    }

    public class GetPreData extends AsyncTask<String, String, String> implements RecyclerViewClickInterface{

        String z = "";
        boolean isSuccess = false;
        ArrayList<StaticRecyclerViewModel> AllAnnexesForOccupy = new ArrayList<>();

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
                        String query = "SELECT * FROM Client WHERE NIC = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";

                            String queryFetchData = "SELECT * FROM Annex WHERE NIC = '" + passingID + "' and IsOccupied = 'No';";
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

                                AllAnnexesForOccupy.add(new StaticRecyclerViewModel(R.drawable.annex_ad_1, "Annex in " + resultSetFetchAnnexes.getString("City"), "Rooms : " + resultSetFetchAnnexes.getString("NoOfRooms"), "Key Money : Rs." + resultSetFetchAnnexes.getString("KeyMoney"), resultSetFetchAnnexes.getString("RentalPerMonth"), rating,AnnexID,"0"));

                            }
                            AllAnnexesForOccupierRecycler = findViewById(R.id.RecyclerViewSelectAnnex);
                            AllAnnexesOccupyAdapter = new StaticRecyclerViewAdapter(AllAnnexesForOccupy,this);
                            AllAnnexesForOccupierRecycler.setLayoutManager(new LinearLayoutManager(OccupyPeopleAnnexPanel.this, LinearLayoutManager.HORIZONTAL, false));
                            AllAnnexesForOccupierRecycler.setAdapter(AllAnnexesOccupyAdapter);

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

            SelectedAnnexID = AllAnnexesForOccupy.get(position).getBoardingID();
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
            Toast.makeText(OccupyPeopleAnnexPanel.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(OccupyPeopleAnnexPanel.this , "All Done" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SearchMenu.class);

                //Animation

                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String FirstNameOC = FirstName.getText().toString();
            String LastNameOC = LastName.getText().toString();
            String PhoneNumberOC = PhoneNumber.getText().toString();
            String GuardianPhoneOC = GuardianPhone.getText().toString();
            String AddressLine1OC = AddressLine1.getText().toString();
            String AddressLine2OC = AddressLine2.getText().toString();
            String CityOC = City.getText().toString();
            String Day = Integer.toString(OccupyDate.getDayOfMonth());
            String Month = Integer.toString(OccupyDate.getMonth());
            String Year = Integer.toString(OccupyDate.getYear());

            if(FirstNameOC.equals("")){
                z = "Passwords are not match";
            }
            else{

                if(FirstNameOC.trim().equals("")){

                    z = "Please enter valid username and password";

                }
                else{
                    try {
                        connection = connectionclass(User,Pass,DB,IP,PORT);

                        if(connection == null){
                            z = "turn on cellular data or Wi-Fi";
                        }
                        else{
                            String query = "insert into NonUserOccupierAnnex(OccupierID,AnnexID,OccupiedDate,FirstName,LastName,PhoneNumber,GuardianPhoneNumber,Gender,AddressLine1,AddressLine2,City) values ('" + OccupierID + "','" + SelectedAnnexID + "','" + Year + "-" + Month + "-" + Day + "','" + FirstNameOC +"','" + LastNameOC + "','" + PhoneNumberOC + "','" + GuardianPhoneOC + "','" + GenderV + "','" + AddressLine1OC + "','" + AddressLine2OC + "','" + CityOC + "');";
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(query);

                            String queryOccupyAnnexTable = "update Annex set IsOccupied = 'Yes' where AnnexID = '" + SelectedAnnexID + "';";
                            statement.executeUpdate(queryOccupyAnnexTable);

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