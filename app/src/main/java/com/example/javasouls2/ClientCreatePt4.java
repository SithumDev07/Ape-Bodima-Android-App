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

public class ClientCreatePt4 extends AppCompatActivity implements DatabaseCredentials{
    //Variables
    private Button photoSelect;
    private Button upload;
    private ImageView mainImage;
    private Button skipStep;
    private String passingID;
    private Bitmap bitmap;
    private String encodedImage;

    private ImageView backBtn;
    private TextView Title;

    private byte[] imageBytes;

    private Button finishBtn,LogBack;
    private Connection connection;
    private Statement statement;

    public static final String EXTRA_TEXT = "com.example.javasouls2.ClientCreatePt4.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_create_pt4);

        //Hooks
        photoSelect = (Button) findViewById(R.id.SelectPictureBtn);
        upload = (Button) findViewById(R.id.UploadPictureBtn);
        mainImage = (ImageView) findViewById(R.id.ProfilePictureClient);
        skipStep = (Button) findViewById(R.id.SkipBtn);

        //Transition Items
        backBtn = (ImageView) findViewById(R.id.create_Acc_backButton);
        Title = (TextView) findViewById(R.id.CreateAccTextTitle);
        finishBtn = (Button) findViewById(R.id.signupFinish);
        LogBack = (Button) findViewById(R.id.BackToLoginButton);

        SessionManager sessionManager = new SessionManager(ClientCreatePt4.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);


    }

    public void SelectPhoto(View view){

        Dexter.withActivity(ClientCreatePt4.this)
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

    public void CallNextWindow(View view){
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");
    }

    public void callLoginback(View view){
        Intent intent = new Intent(getApplicationContext(),ClientLoginWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View,String>(findViewById(R.id.BackToLoginButton),"transition_client");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreatePt4.this,pairs);

        startActivity(intent,options.toBundle());
    }

    public void RedirectClientDashboard(View view){
        Intent intent = new Intent(getApplicationContext(),PreLoginAfterCreate.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View,String>(findViewById(R.id.SkipBtn),"prelogin");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreatePt4.this,pairs);

        startActivity(intent,options.toBundle());
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
            Toast.makeText(ClientCreatePt4.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(ClientCreatePt4.this , "Hooray!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),PreLoginAfterCreate.class);

                Pair[] pairs = new Pair[1];

                //Since we don't need any use of Client Area Button
                pairs[0] = new Pair<View,String>(findViewById(R.id.UploadPictureBtn),"prelogin");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreatePt4.this,pairs);

                startActivity(intent,options.toBundle());
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
                        String query = "UPDATE CLIENT SET ProfilePicture = ? where NIC = '" + passingID + "';";



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