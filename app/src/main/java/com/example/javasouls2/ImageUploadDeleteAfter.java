package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ImageUploadDeleteAfter extends AppCompatActivity implements DatabaseCredentials{

    private Connection connection;
    private TextView textView;
    private EditText editTextIndex;
    private EditText editTextFileName;
    private ImageView imageView;
    private String passingID;

    private byte[] bytesImage;

    private Statement statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload_delete_after);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        textView = findViewById(R.id.Title);
        editTextIndex = findViewById(R.id.Index);
        editTextFileName = findViewById(R.id.PhotoIndex);
        imageView = findViewById(R.id.Image);

        SessionManager sessionManager = new SessionManager(ImageUploadDeleteAfter.this,SessionManager.SESSION_REMEMBERME);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONNIC);

        try {
            connection = connectionclass(User,Pass,DB,IP,PORT);

            if(connection == null){
                textView.setText("Failed");
                //z = "turn on cellular data or Wi-Fi";
            }
            else{
                textView.setText("Success");

            }
            //isSuccess = true;
            connection.close();
        }
        catch (Exception e){
           // isSuccess = false;
            //z = e.getMessage();
        }

    }

    public void InsertImage(View view){

        String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + editTextFileName.getText().toString() + ".jpg";

        Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);

        imageView.setImageBitmap(bitmap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);

        bytesImage = byteArrayOutputStream.toByteArray();

        boolean isSuccess = false;
        String z = "";

        try {
            connection = connectionclass(User,Pass,DB,IP,PORT);

            if(connection == null){
                textView.setText("Failed");
                z = "turn on cellular data or Wi-Fi";
            }
            else{
                textView.setText("SuccessUp");
                String query = "INSERT INTO testImage(PhotoName,Photo) VALUES (?,?);";

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setBytes(2,bytesImage);
                preparedStatement.setString(1,"Name");

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