package com.example.javasouls2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.javasouls2.Client.AddBoardingHouseRoomsWindow;
import com.example.javasouls2.Client.AddBoardingHouseWindow;
import com.example.javasouls2.Client.SplashScreenAfterAnnex;
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
import java.util.HashMap;

public class AddPhotoBoardingHouseRooms extends AppCompatActivity implements DatabaseCredentials{

    private Bitmap bitmap;
    private byte[] imageBytes1;
    private byte[] imageBytes2;
    private byte[] imageBytes3;
    private byte[] imageBytes4;
    private byte[] imageBytes5;

    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;
    private ImageView Image4;
    private ImageView Image5;


    private Connection connection;
    private String passingID;
    private String RoomID;

    public static final String EXTRA_TEXT = "com.example.javasouls2.AddPhotoAnnex.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_photo_boarding_house_rooms);

        Image1 = (ImageView) findViewById(R.id.AnnexPhoto1);
        Image2 = (ImageView) findViewById(R.id.AnnexPhoto2);
        Image3 = (ImageView) findViewById(R.id.AnnexPhoto3);
        Image4 = (ImageView) findViewById(R.id.AnnexPhoto4);
        Image5 = (ImageView) findViewById(R.id.AnnexPhoto5);


        Intent intentData = getIntent();
        RoomID = intentData.getStringExtra(AddBoardingHouseRoomsWindow.EXTRA_TEXT);

        SessionManager sessionManager = new SessionManager(AddPhotoBoardingHouseRooms.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);


    }
    public void AddAnother(View view){

        AddAnotherAd addAnotherAd = new AddAnotherAd();
        addAnotherAd.execute("");

    }

    public void UploadPhotos(View view){
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");
    }

    public void SelectPhoto1(View view){

        Dexter.withActivity(AddPhotoBoardingHouseRooms.this)
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

    public void SelectPhoto2(View view){

        Dexter.withActivity(AddPhotoBoardingHouseRooms.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Image"),2);
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

    public void SelectPhoto3(View view){


        Dexter.withActivity(AddPhotoBoardingHouseRooms.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Image"),3);
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

    public void SelectPhoto4(View view){

        Dexter.withActivity(AddPhotoBoardingHouseRooms.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Image"),4);
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

    public void SelectPhoto5(View view){


        Dexter.withActivity(AddPhotoBoardingHouseRooms.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Image"),5);
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
                Image1.setImageBitmap(bitmap);

                imageStore(bitmap,1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Image2.setImageBitmap(bitmap);

                imageStore(bitmap,2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == 3 && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Image3.setImageBitmap(bitmap);

                imageStore(bitmap,3);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == 4 && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Image4.setImageBitmap(bitmap);

                imageStore(bitmap,4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == 5 && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Image5.setImageBitmap(bitmap);

                imageStore(bitmap,5);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap,int position) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);

        if(position == 1){

            imageBytes1 = stream.toByteArray();

            String encodedImage = android.util.Base64.encodeToString(imageBytes1, Base64.DEFAULT);
        }
        if(position == 2){
            imageBytes2 = stream.toByteArray();

            String encodedImage = android.util.Base64.encodeToString(imageBytes1, Base64.DEFAULT);
        }
        if(position == 3){
            imageBytes3 = stream.toByteArray();

            String encodedImage = android.util.Base64.encodeToString(imageBytes1, Base64.DEFAULT);
        }
        if(position == 4){

            imageBytes4 = stream.toByteArray();

            String encodedImage = android.util.Base64.encodeToString(imageBytes1, Base64.DEFAULT);
        }
        if(position == 5){
            imageBytes5 = stream.toByteArray();

            String encodedImage = android.util.Base64.encodeToString(imageBytes1, Base64.DEFAULT);
        }
    }

    public class CheckLogin extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(AddPhotoBoardingHouseRooms.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(AddPhotoBoardingHouseRooms.this , "Hooray!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),SplashScreenAfterAnnex.class);


                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {


            if(RoomID.trim().equals("")){


                z = "There is an error with the image";

            }
            else{
                try {
                    connection = connectionclass();

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "INSERT INTO BoardingHouseRoomsPhoto(RoomID,Photo) values ('" + RoomID + "',?)";


                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        // preparedStatement.setString(1,"Second");
                        preparedStatement.setBytes(1,imageBytes1);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes2);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes3);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes4);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes5);
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

    public class AddAnotherAd extends AsyncTask<String,String,String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(AddPhotoBoardingHouseRooms.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(AddPhotoBoardingHouseRooms.this , "Hooray!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddedBoardingSplash.class);


                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {


            if(RoomID.trim().equals("")){

                z = "There is an error with the image";

            }
            else{
                try {
                    connection = connectionclass();

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "INSERT INTO BoardingHouseRoomsPhoto(RoomID,Photo) values ('" + RoomID + "',?)";


                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        // preparedStatement.setString(1,"Second");
                        preparedStatement.setBytes(1,imageBytes1);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes2);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes3);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes4);
                        preparedStatement.execute();

                        preparedStatement.setBytes(1,imageBytes5);
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