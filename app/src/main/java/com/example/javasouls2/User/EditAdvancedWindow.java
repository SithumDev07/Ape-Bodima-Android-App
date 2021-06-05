package com.example.javasouls2.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.EditAccountWindow;
import com.example.javasouls2.R;
import com.example.javasouls2.SessionManager;
import com.example.javasouls2.UserDashboard;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

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

public class EditAdvancedWindow extends AppCompatActivity implements DatabaseCredentials {

    private EditText Password;
    private EditText Guardian;
    private EditText AddressLine1;
    private EditText AddressLine2;
    private EditText City;
    private EditText PostalCode;

    private Button UpdateData;

    private RelativeLayout CardView;
    private ImageView Profile;
    private TextView FullName;
    private TextView NIC;

    private String passingID;
    private Connection connection;

    private Bitmap bitmap;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_advanced_window);

        Password = (EditText) findViewById(R.id.PasswordInputUserAdvanced);
        Guardian = (EditText) findViewById(R.id.GuardianInputUserAdvanced);
        AddressLine1 = (EditText) findViewById(R.id.Line1InputUserAdvanced);
        AddressLine2 = (EditText) findViewById(R.id.Line2InputUserAdvanced);
        City = (EditText) findViewById(R.id.CityInputUserAdvanced);
        PostalCode = (EditText) findViewById(R.id.PostalInputUserAdvanced);

        UpdateData = (Button) findViewById(R.id.Update);

        CardView = (RelativeLayout) findViewById(R.id.AdCardView);
        Profile = (ImageView) findViewById(R.id.UserProfile);
        FullName = (TextView) findViewById(R.id.FullName);
        NIC = (TextView) findViewById(R.id.NIC);

        SessionManager sessionManager = new SessionManager(EditAdvancedWindow.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        SetPreData setPreData = new SetPreData();
        setPreData.execute("");
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), EditAccountWindow.class);

        Pair[] pairs = new Pair[9];

        pairs[0] = new Pair<View, String>(Profile, "userProfile");
        pairs[1] = new Pair<View, String>(FullName, "FullName");
        pairs[2] = new Pair<View, String>(NIC, "UserNIC");
        pairs[3] = new Pair<View, String>(CardView, "CardView");
        pairs[4] = new Pair<View, String>(Password, "FirstName");
        pairs[5] = new Pair<View, String>(Guardian, "LastName");
        pairs[6] = new Pair<View, String>(AddressLine1, "PhoneNumber");
        pairs[7] = new Pair<View, String>(AddressLine2, "Email");
        pairs[8] = new Pair<View, String>(UpdateData, "UpdateBtn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EditAdvancedWindow.this, pairs);
        startActivity(intent, options.toBundle());



    }

    public void ChangeProfile(View view){
        Dexter.withActivity(EditAdvancedWindow.this)
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
                Profile.setImageBitmap(bitmap);

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

        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);


        try {
            connection = connectionclass(User, Pass, DB, IP, PORT);

            if (connection == null) {

            } else {

                String UpdatePhoto = "UPDATE AppUser SET ProfilePicture = ? where Username = '" + passingID + "';";
                PreparedStatement preparedStatement = connection.prepareStatement(UpdatePhoto);

                // preparedStatement.setString(1,"Second");
                preparedStatement.setBytes(1,imageBytes);
                preparedStatement.execute();
            }

            connection.close();
        } catch (Exception e) {

        }

    }


    public class SetPreData extends AsyncTask<String, String, String> {

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
                        String query = "SELECT * FROM AppUser WHERE Username = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successful";
                            String FullNameText = resultSet.getString("FirstName") + " " + resultSet.getString("LastName");
                            FullName.setText(FullNameText);
                            NIC.setText(passingID);

                            Password.setText(resultSet.getString("UserPassword"));

                            Guardian.setText(resultSet.getString("GuardianPhoneNumber"));
                            AddressLine1.setText(resultSet.getString("AddressLine1"));
                            AddressLine2.setText(resultSet.getString("AddressLine2"));
                            City.setText(resultSet.getString("City"));
                            PostalCode.setText(resultSet.getString("PostalCode"));

                            String fetchProfilePicture = "SELECT ProfilePicture from AppUser where UserName = '" + passingID + "';";
                            ResultSet resultSetImage = statement.executeQuery(fetchProfilePicture);
                            resultSetImage.next();

                            byte[] byteImageDb = resultSetImage.getBytes("ProfilePicture");

                            Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                            Profile.setImageBitmap(bitmapImageDB);

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

    public void UpdateAccount(View view){

        UpdateAccount updateAccount = new UpdateAccount();
        updateAccount.execute("");

    }

    public class UpdateAccount extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(EditAdvancedWindow.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(EditAdvancedWindow.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);

                Pair[] pairs = new Pair[1];

                //Since we don't need any use of Client Area Button
                pairs[0] = new Pair<View, String>(findViewById(R.id.MainLayout), "dashboard");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EditAdvancedWindow.this, pairs);
                startActivity(intent, options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String PasswordUser = Password.getText().toString();
            String guardianPhone = Guardian.getText().toString();
            String Address = AddressLine1.getText().toString();
            String Line2 = AddressLine2.getText().toString();
            String CityUser = City.getText().toString();
            String Postal = PostalCode.getText().toString();

            if (passingID.trim().equals("")) {

                z = "Fatal Error = passingID not found";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "Turn On Your Cellular Data Or Wi-Fi";
                    } else {
                        String query = "UPDATE AppUser SET UserPassword = '" + PasswordUser + "',GuardianPhoneNumber = '" + guardianPhone + "',AddressLine1 = '" + Address + "',AddressLine2 = '" + Line2 + "',City = '" + CityUser + "',PostalCode = '" + Postal + "' where Username = '" + passingID + "';";
                        Statement statement = connection.createStatement();

                        statement.executeUpdate(query);

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

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server, String port){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");


            // ConnectionURL = "jdbc:jtds:sqlserver://apebodima.database.windows.net:1433;DatabaseName=ApeBodima;user=adminApeBodima@apebodima;password=Fr13nds2552;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";


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