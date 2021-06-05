package com.example.javasouls2.Client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.R;
import com.example.javasouls2.SearchMenu;
import com.example.javasouls2.SessionManager;
import com.example.javasouls2.UserPhoto;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ClientEditAccountWindow extends AppCompatActivity implements DatabaseCredentials {

    private ImageView backBtn;
    private TextView mainTitle;
    private TextView firstNameView;
    private TextView ClientIDView;

    private EditText firstName;
    private EditText lastName;
    private EditText mainPhoneNumber;
    private EditText homePhoneNumber;
    private EditText emailAddressClient;
    private EditText passwordChangeable;


    private de.hdodenhof.circleimageview.CircleImageView ClientProfile;

    private Button updateBtn;
    private Bitmap bitmap;
    private byte[] imageBytes;

    private String passingID;
    private Connection connection;

    private String MainPhoneClient;
    private String HomePhoneClient;

    public static final String EXTRA_TEXT = "com.example.javasouls2.ClientEditAccountWindow.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_edit_account_window);

        backBtn = (ImageView) findViewById(R.id.edit_Acc_backButton);
        mainTitle = (TextView) findViewById(R.id.EditAccTextTitle);
        ClientProfile = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        firstNameView = (TextView) findViewById(R.id.FullName);
        ClientIDView = (TextView) findViewById(R.id.NIC);

        firstName = (EditText) findViewById(R.id.FirstName);
        lastName = (EditText) findViewById(R.id.LastName);
        mainPhoneNumber = (EditText) findViewById(R.id.MainPhoneNumber);
        homePhoneNumber = (EditText) findViewById(R.id.HomePhoneNumber);
        emailAddressClient = (EditText) findViewById(R.id.EmailAddress);
        passwordChangeable = (EditText) findViewById(R.id.changePassword);

        updateBtn = (Button) findViewById(R.id.Update);

        SessionManager sessionManager = new SessionManager(ClientEditAccountWindow.this, SessionManager.SESSION_REMEMBERME);
        HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        SetPreData setPreData = new SetPreData();
        setPreData.execute("");

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAccount updateAccount = new UpdateAccount();
                updateAccount.execute("");
            }
        });
    }

    public void BackToSearchMenu(View view) {
        Intent intentSearch = new Intent(getApplicationContext(), SearchMenu.class);

        startActivity(intentSearch);
    }

    public class SetPreData extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {


            if (passingID.trim().equals("")) {

                z = "Passing ID Failure";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "SELECT * FROM CLIENT WHERE NIC = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Loading Data";




                            String FullNameText = resultSet.getString("FirstName") + " " + resultSet.getString("LastName");
                            firstNameView.setText(FullNameText);

                            ClientIDView.setText(resultSet.getString("NIC"));

                            firstName.setText(resultSet.getString("FirstName"));
                            lastName.setText(resultSet.getString("LastName"));


                            emailAddressClient.setText(resultSet.getString("Email"));
                            passwordChangeable.setText(resultSet.getString("Password1"));

                            String queryGetPhoneNumbers = "SELECT TOP 1 * FROM CLIENTPhoneNumber WHERE NIC = '" + passingID + "';";
                            ResultSet resultSetMainNumber = statement.executeQuery(queryGetPhoneNumbers);

                            if (resultSetMainNumber.next()) {

                                mainPhoneNumber.setText(resultSetMainNumber.getString("PhoneNumber"));
                                MainPhoneClient = mainPhoneNumber.getText().toString();

                                String queryGetPhoneNumberHome = "SELECT TOP 1 * FROM CLIENTPhoneNumber WHERE NIC = '" + passingID + "' order by PhoneNumber desc;";
                                ResultSet resultSetHomeNumber = statement.executeQuery(queryGetPhoneNumberHome);

                                if (resultSetHomeNumber.next()) {

                                    homePhoneNumber.setText(resultSetHomeNumber.getString("PhoneNumber"));
                                    HomePhoneClient = homePhoneNumber.getText().toString();

                                }

                            }

                            String fetchProfilePicture = "SELECT ProfilePicture from Client where NIC = '" + passingID + "';";
                            ResultSet resultSetImage = statement.executeQuery(fetchProfilePicture);
                            resultSetImage.next();

                            byte[] byteImageDb = resultSetImage.getBytes("ProfilePicture");

                            Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                            ClientProfile.setImageBitmap(bitmapImageDB);

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

    public class UpdateAccount extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(ClientEditAccountWindow.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(ClientEditAccountWindow.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SearchMenu.class);

                Pair[] pairs = new Pair[2];

                pairs[0] = new Pair<View, String>(mainTitle, "mainTitle");
                pairs[1] = new Pair<View, String>(ClientProfile, "userProfile");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientEditAccountWindow.this, pairs);
                startActivity(intent, options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String FirstName = firstName.getText().toString();
            String LastName = lastName.getText().toString();
            String MainPhone = mainPhoneNumber.getText().toString();
            String HomePhone = homePhoneNumber.getText().toString();
            String eMail = emailAddressClient.getText().toString();
            String PasswordSet = passwordChangeable.getText().toString();

            if (passingID.trim().equals("")) {

                z = "FATAL : Passing ID Failure!!!";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "UPDATE Client SET FirstName = '" + FirstName + "',LastName = '" + LastName + "',Email = '" + eMail + "',Password1 = '" + PasswordSet + "' WHERE NIC = '" + passingID + "';";
                        String queryPhoneNumberUpdate = "UPDATE ClientPhoneNumber SET PhoneNumber = '" + MainPhone + "' WHERE NIC = '" + passingID + "' and PhoneNumber = '" + MainPhoneClient + "';";
                        String queryPhoneNumberUpdateHome = "UPDATE ClientPhoneNumber SET PhoneNumber = '" + HomePhone + "' WHERE NIC = '" + passingID + "' and PhoneNumber = '" + HomePhoneClient + "';";


                        Statement statement = connection.createStatement();



                        statement.executeUpdate(query);
                        statement.executeUpdate(queryPhoneNumberUpdate);
                        statement.executeUpdate(queryPhoneNumberUpdateHome);


                    }
                    isSuccess = true;
                    connection.close();
                } catch (Exception e) {
                    isSuccess = false;
                    z = e.getMessage();
                }
            }

            return z;
        }
    }

    public void ChangeProfile(View view){
        Dexter.withActivity(ClientEditAccountWindow.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                ClientProfile.setImageBitmap(bitmap);

                imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);

        imageBytes = stream.toByteArray();

        //String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);


        try {
            connection = connectionclass(User, Pass, DB, IP, PORT);

            if (connection == null) {

            } else {

                String UpdatePhoto = "UPDATE Client SET ProfilePicture = ? where NIC = '" + passingID + "';";
                PreparedStatement preparedStatement = connection.prepareStatement(UpdatePhoto);

                // preparedStatement.setString(1,"Second");
                preparedStatement.setBytes(1,imageBytes);
                preparedStatement.execute();
            }

            connection.close();
        } catch (Exception e) {

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