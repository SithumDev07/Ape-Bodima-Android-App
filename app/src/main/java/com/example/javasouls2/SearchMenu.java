package com.example.javasouls2;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.javasouls2.Client.AllAnnexes;
import com.example.javasouls2.Client.AllBoardings;
import com.example.javasouls2.Client.ClientEditAccountWindow;
import com.example.javasouls2.Client.OccupyPeoplePanel;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SearchMenu extends AppCompatActivity implements DatabaseCredentials, NavigationView.OnNavigationItemSelectedListener {


    private String passingID;
    private String WelcomeText;
    private Connection connection;
    private TextView PreWelcome;
    private ImageView editAccountWindow;
    private ImageView drawer;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private RelativeLayout panel;
    private RelativeLayout OccupyPanel;
    private ImageView logoPanel;
    private TextView titlePanel;

    public static final String EXTRA_TEXT = "com.example.javasouls2.SearchMenu.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_menu);

        //Hooks
        PreWelcome = (TextView) findViewById(R.id.WelcomeTitle);
        editAccountWindow = (ImageView) findViewById(R.id.editAccountBtn);
        panel = (RelativeLayout) findViewById(R.id.PanelWindow);
        OccupyPanel = (RelativeLayout) findViewById(R.id.OccupyPeopleLayout);
        logoPanel = (ImageView) findViewById(R.id.logo);
        titlePanel = (TextView) findViewById(R.id.panelTitle);

        drawer = (ImageView) findViewById(R.id.toolBar);
        //Drawer menu hooks
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


        SessionManager sessionManager = new SessionManager(SearchMenu.this, SessionManager.SESSION_REMEMBERME);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");

    }

    public void OpenDrawer(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void MoveToPanelActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ClientPanel.class);

        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View, String>(logoPanel, "logo_image");
        pairs[1] = new Pair<View, String>(titlePanel, "title");
        pairs[2] = new Pair<View, String>(panel, "panel");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchMenu.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void MoveToAllBoardingsPanel(View view) {

        Intent intentAllBoardings = new Intent(getApplicationContext(), AllBoardings.class);

        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View, String>(drawer, "drawer");
        pairs[1] = new Pair<View, String>(PreWelcome, "mainTitle");
        pairs[2] = new Pair<View, String>(editAccountWindow, "userProfile");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchMenu.this, pairs);

        startActivity(intentAllBoardings,options.toBundle());

    }

    public void MoveToAllAnnexesPanel(View view){

        Intent intentAllBoardings = new Intent(getApplicationContext(), AllAnnexes.class);

        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View, String>(drawer, "drawer");
        pairs[1] = new Pair<View, String>(PreWelcome, "mainTitle");
        pairs[2] = new Pair<View, String>(editAccountWindow, "userProfile");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchMenu.this, pairs);

        startActivity(intentAllBoardings,options.toBundle());
    }

    public void MoveToOccupyPanel(View view){

        Intent intentAllBoardings = new Intent(getApplicationContext(), OccupyPeoplePanel.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(OccupyPanel, "OpenOccupy");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchMenu.this, pairs);

        startActivity(intentAllBoardings,options.toBundle());
    }

    public void moveToClientEditAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), ClientEditAccountWindow.class);

        Pair[] pairs = new Pair[2];

        pairs[0] = new Pair<View, String>(PreWelcome, "mainTitle");
        pairs[1] = new Pair<View, String>(editAccountWindow, "userProfile");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchMenu.this, pairs);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

                Intent intentAccount = new Intent(getApplicationContext(), ClientEditAccountWindow.class);

                Pair[] pairs = new Pair[2];

                pairs[0] = new Pair<View, String>(PreWelcome, "mainTitle");
                pairs[1] = new Pair<View, String>(editAccountWindow, "userProfile");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchMenu.this, pairs);
                intentAccount.putExtra(EXTRA_TEXT, passingID);
                startActivity(intentAccount, options.toBundle());
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

                Intent intent = new Intent(getApplicationContext(), ClientLoginWindow.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public class CheckLogin extends AsyncTask<String, String, String> {

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
                        String query = "SELECT * FROM Client WHERE NIC= '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successfull";
                            WelcomeText = resultSet.getString("FirstName");
                            PreWelcome.setText("Welcome " + WelcomeText);
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