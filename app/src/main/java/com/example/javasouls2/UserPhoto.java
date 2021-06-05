package com.example.javasouls2;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class UserPhoto extends AppCompatActivity implements DatabaseCredentials{

    private Button upload;
    private ImageView mainImage;
    private TextView MainTitle;
    private Button skipStep;
    private Button LogBack;
    private ImageView backBtn;

    private Bitmap bitmap;
    private String encodedImage;
    private byte[] imageBytes;

    private String passingID;
    private Connection connection;
    private Statement statement;

    public static final String EXTRA_TEXT = "com.example.javasouls2.UserPhoto.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_photo);

        upload = (Button) findViewById(R.id.UploadPictureBtnUser);
        mainImage = (ImageView) findViewById(R.id.ProfilePictureUser);
        skipStep = (Button) findViewById(R.id.SkipBtnUser);
        LogBack = (Button) findViewById(R.id.BackToLoginButtonUser);


        //Transition Items
        backBtn = (ImageView) findViewById(R.id.create_Acc_backButton);
        MainTitle = (TextView) findViewById(R.id.LogoNameUser);


        SessionManager sessionManager = new SessionManager(UserPhoto.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);


    }

    public void SelectPhoto(View view){
        Dexter.withActivity(UserPhoto.this)
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
                mainImage.setImageBitmap(bitmap);

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

        encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }


    public void Finish(View view){

        UploadUserPhoto uploadUserPhoto = new UploadUserPhoto();
        uploadUserPhoto.execute("");

    }

    public void BackOneStep(View view){

        Intent intent = new Intent(getApplicationContext(), UserCreateAcc2.class);

        startActivity(intent);

    }

    public void RedirectUserDashboard(View view){

        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);

        startActivity(intent);

    }

    public void CallLogInBack(View view){

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(intent);

    }

    public class UploadUserPhoto extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(UserPhoto.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(UserPhoto.this , "Hooray!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);

                startActivity(intent);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {


            if(passingID.trim().equals("")){

                z = "There is an error with the image";

            }
            else{
                try {
                    connection = connectionclass();

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "UPDATE AppUser SET ProfilePicture = ? where Username = '" + passingID + "';";



                        PreparedStatement preparedStatement = connection.prepareStatement(query);

                        // preparedStatement.setString(1,"Second");
                        preparedStatement.setBytes(1,imageBytes);

                        preparedStatement.execute();

                    }
                    isSuccess = true;
                    connection.close();
                }
                catch (Exception e){
                    isSuccess = false;
                    z = e.getMessage();
                }
            }

            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(){
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