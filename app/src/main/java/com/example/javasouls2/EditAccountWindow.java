package com.example.javasouls2;

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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javasouls2.Client.ClientEditAccountWindow;
import com.example.javasouls2.User.EditAdvancedWindow;
import com.google.android.material.card.MaterialCardView;
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

public class EditAccountWindow extends AppCompatActivity implements DatabaseCredentials {

    //Variables
    private ImageView ProfilePicture;
    private TextView FullName;
    private TextView UserID;
    private RelativeLayout CardView;

    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText emailAddress;

    private Bitmap bitmap;
    private byte[] imageBytes;

    private MaterialCardView favorites;
    private MaterialCardView AdvancedUpdates;

    private Button updateData;


    private TextView TitleAD;
    private de.hdodenhof.circleimageview.CircleImageView ImageAD;
    private RatingBar ratingAD;
    private TextView RentalAD;
    private TextView Title3AD;
    private RatingBar RatingUser;
    private float Rating = 0.0f;
    private Button RateBtn;
    private TextView CurrentlyText;
    private com.chinodev.androidneomorphframelayout.NeomorphFrameLayout layoutOccupiedAd;

    private Connection connection;
    private String passingID;
    private String OccupiedPlaceID;

    public static final String EXTRA_TEXT = "com.example.javasouls2.EditAccountWindow.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_account_window);

        //Hooks
        ProfilePicture = (ImageView) findViewById(R.id.UserProfile);
        FullName = (TextView) findViewById(R.id.FullName);
        UserID = (TextView) findViewById(R.id.NIC);
        CardView = (RelativeLayout) findViewById(R.id.RelativeAdCard);

        firstName = (EditText) findViewById(R.id.FirstName);
        lastName = (EditText) findViewById(R.id.LastName);
        phoneNumber = (EditText) findViewById(R.id.MainPhoneNumber);
        emailAddress = (EditText) findViewById(R.id.EmailAddress);

        favorites = (MaterialCardView) findViewById(R.id.FavouritesCardView);
        AdvancedUpdates = (MaterialCardView) findViewById(R.id.AdvancedUpdatesCardView);

        updateData = (Button) findViewById(R.id.Update);


        //Currently Live and Rating
        TitleAD = (TextView) findViewById(R.id.TitleADUser);
        ImageAD = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.ADImageUser);
        ratingAD = (RatingBar) findViewById(R.id.RatingadUser);
        RentalAD = (TextView) findViewById(R.id.RentalADUser);
        Title3AD =  (TextView) findViewById(R.id.Title3UserAD);
        RatingUser = (RatingBar) findViewById(R.id.RatingGiveUser);
        RateBtn = (Button) findViewById(R.id.GiveRate);

        CurrentlyText = (TextView) findViewById(R.id.TextLiving);
        layoutOccupiedAd = (com.chinodev.androidneomorphframelayout.NeomorphFrameLayout) findViewById(R.id.OccupyAd);

        RatingUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Rating = rating;
            }
        });

        SessionManager sessionManager = new SessionManager(EditAccountWindow.this,SessionManager.SESSION_REMEMBERMEUSER);
        HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsUserFromSession();
        passingID = rememberMeDetails.get(SessionManager.KEY_SESSIONUSER);

        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");

        CurrentlyLive currentlyLive = new CurrentlyLive();
        currentlyLive.execute("");

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAccount updateAccount = new UpdateAccount();
                updateAccount.execute("");
            }
        });
    }

    public void ChangeProfile(View view){
        Dexter.withActivity(EditAccountWindow.this)
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
                ProfilePicture.setImageBitmap(bitmap);

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

    public void ExpandEditAdvanced(View view){

        Intent intent = new Intent(getApplicationContext(), EditAdvancedWindow.class);

        Pair[] pairs = new Pair[9];

        pairs[0] = new Pair<View, String>(ProfilePicture, "userProfile");
        pairs[1] = new Pair<View, String>(FullName, "FullName");
        pairs[2] = new Pair<View, String>(UserID, "UserNIC");
        pairs[3] = new Pair<View, String>(CardView, "CardView");
        pairs[4] = new Pair<View, String>(firstName, "FirstName");
        pairs[5] = new Pair<View, String>(lastName, "LastName");
        pairs[6] = new Pair<View, String>(phoneNumber, "PhoneNumber");
        pairs[7] = new Pair<View, String>(emailAddress, "Email");
        pairs[8] = new Pair<View, String>(updateData, "UpdateBtn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EditAccountWindow.this, pairs);
        startActivity(intent, options.toBundle());

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
        startActivity(intent);
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
                        String query = "SELECT * FROM AppUser WHERE Username = '" + passingID + "';";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if (resultSet.next()) {
                            z = "Login Successfull";
                            String FullNameText = resultSet.getString("FirstName") + " " + resultSet.getString("LastName");
                            FullName.setText(FullNameText);

                            UserID.setText(resultSet.getString("Username"));

                            firstName.setText(resultSet.getString("FirstName"));
                            lastName.setText(resultSet.getString("LastName"));
                            phoneNumber.setText(resultSet.getString("PhoneNumber"));
                            emailAddress.setText(resultSet.getString("UserEmail"));

                            String fetchProfilePicture = "SELECT ProfilePicture from AppUser where UserName = '" + passingID + "';";
                            ResultSet resultSetImage = statement.executeQuery(fetchProfilePicture);
                            resultSetImage.next();

                            byte[] byteImageDb = resultSetImage.getBytes("ProfilePicture");

                            Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                            ProfilePicture.setImageBitmap(bitmapImageDB);

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


    public class CurrentlyLive extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;
        float rating;

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
                        String query = "select top 1 UserBoardingHouseRooms.Username,UserBoardingHouseRooms.RoomID,UserBoardingHouseRooms.OccupiedDate,BoardingHouseRooms.RoomID,BoardingHouse.City,BoardingHouseRooms.RentalPerPerson,BoardingHouse.BoardingID,BoardingHouseRooms.BoardingID,BoardingHouse.NIC,Client.NIC,Client.FirstName,BoardingHouseRoomsPhoto.Photo,BoardingHouseRoomsPhoto.RoomID from UserBoardingHouseRooms,BoardingHouseRooms,BoardingHouse,Client,BoardingHouseRoomsPhoto where UserBoardingHouseRooms.RoomID = BoardingHouseRooms.RoomID and BoardingHouseRooms.BoardingID = BoardingHouse.BoardingID and BoardingHouse.NIC = Client.NIC and UserBoardingHouseRooms.Username = '" + passingID + "' and BoardingHouseRoomsPhoto.RoomID = BoardingHouseRooms.RoomID;";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        String QueryAnnexDetails = "select top 1 UserAnnex.Username,UserAnnex.AnnexID,UserAnnex.OccupiedDate,Annex.AnnexID,Annex.NIC,Client.NIC,Annex.City,Annex.RentalPerMonth,Client.FirstName,AnnexPhoto.AnnexID,AnnexPhoto.PhotoID,AnnexPhoto.Photo from UserAnnex,Annex,Client,AnnexPhoto where UserAnnex.AnnexID = Annex.AnnexID and Annex.NIC = Client.NIC and Annex.AnnexID = AnnexPhoto.AnnexID and UserAnnex.Username = '" + passingID + "';";
                        Statement statementAnnex = connection.createStatement();
                        ResultSet resultSetAnnex = statementAnnex.executeQuery(QueryAnnexDetails);

                        if (resultSet.next()) {
                            z = "Loading Data";
                            String Title = resultSet.getString("FirstName") + "'s boarding in " + resultSet.getString("City");
                            TitleAD.setText(Title);

                           // rating = Float.parseFloat(resultSet.getString("Rating"));
                            //ratingAD.setRating(rating);
                            //Title2AD.setText(resultSet.getString("FirstName"));
                            String Rental = "Rental : Rs." + resultSet.getString("RentalPerPerson");
                            RentalAD.setText(Rental);

                            OccupiedPlaceID = resultSet.getString("RoomID");

                            Title3AD.setText("Occupied since : " + resultSet.getString("OccupiedDate"));

                            byte[] byteImageDb = resultSet.getBytes("Photo");

                            Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                            ImageAD.setImageBitmap(bitmapImageDB);

                            String queryOverallRating = "select avg(Rating) as Rating,RoomID from BoardingRoomRating where RoomID = '" + OccupiedPlaceID + "' group by RoomID;";
                            Statement statementRating = connection.createStatement();
                            ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                            if(resultSetRating.next()){
                                rating = Float.parseFloat(resultSetRating.getString("Rating"));

                                ratingAD.setRating(rating);
                            }


                            isSuccess = true;
                            connection.close();
                        }
                        else if(resultSetAnnex.next()){

                            z = "Loading Data";
                            String Title = resultSetAnnex.getString("FirstName") + "'s annex in " + resultSetAnnex.getString("City");
                            TitleAD.setText(Title);

                            //rating = Float.parseFloat(resultSetAnnex.getString("Rating"));
                            //ratingAD.setRating(rating);
                            //Title2AD.setText(resultSet.getString("FirstName"));
                            String Rental = "Rental : Rs." + resultSetAnnex.getString("RentalPerMonth");
                            RentalAD.setText(Rental);


                            Title3AD.setText("Occupied since : " + resultSetAnnex.getString("OccupiedDate"));

                            OccupiedPlaceID = resultSetAnnex.getString("AnnexID");

                            byte[] byteImageDb = resultSetAnnex.getBytes("Photo");

                            Bitmap bitmapImageDB = BitmapFactory.decodeByteArray(byteImageDb,0,byteImageDb.length);


                            ImageAD.setImageBitmap(bitmapImageDB);


                            String queryOverallRating = "select avg(Rating) as Rating,AnnexID from AnnexRating where AnnexID = '" + OccupiedPlaceID+ "' group by AnnexID;";
                            Statement statementRating = connection.createStatement();
                            ResultSet resultSetRating = statementRating.executeQuery(queryOverallRating);

                            if(resultSetRating.next()){
                                rating = Float.parseFloat(resultSetRating.getString("Rating"));

                                ratingAD.setRating(rating);
                            }

                            isSuccess = true;
                            connection.close();

                        }
                        else {
                            Toast.makeText(EditAccountWindow.this,"User is not occupied",Toast.LENGTH_SHORT).show();

                            RatingUser.setVisibility(View.GONE);
                            RateBtn.setVisibility(View.GONE);
                            CurrentlyText.setVisibility(View.GONE);
                            layoutOccupiedAd.setVisibility(View.GONE);
                            TitleAD.setVisibility(View.GONE);
                            RentalAD.setVisibility(View.GONE);
                            ImageAD.setVisibility(View.GONE);
                            ratingAD.setVisibility(View.GONE);

                            z = "The User is not occupied";
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
            Toast.makeText(EditAccountWindow.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(EditAccountWindow.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);

                Pair[] pairs = new Pair[1];

                //Since we don't need any use of Client Area Button
                pairs[0] = new Pair<View, String>(findViewById(R.id.Update), "editAccount");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EditAccountWindow.this, pairs);
                startActivity(intent, options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String FirstName = firstName.getText().toString();
            String LastName = lastName.getText().toString();
            String MainPhone = phoneNumber.getText().toString();
            String eMail = emailAddress.getText().toString();

            if (FirstName.trim().equals("") || LastName.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {
                        String query = "UPDATE AppUser SET FirstName = '" + FirstName + "',LastName = '" + LastName + "',PhoneNumber = '" + MainPhone + "',UserEmail = '" + eMail + "' where Username = '" + passingID + "';";
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

    public void RatePlace(View view){

        UpdateRating updateRating = new UpdateRating();
        updateRating.execute("");

    }

    public class UpdateRating extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(EditAccountWindow.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(EditAccountWindow.this, "Thank you for your support", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);


                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String IDType = OccupiedPlaceID.substring(0,3);
            String Query;
            String OccupierCount = "";

            if(IDType.equals("AID")){

                String getOccupierCount = "select top 1 * from AnnexRating where Username = '" + passingID + "' order by Occupier desc;";
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {

                        Statement statement = connection.createStatement();
                        ResultSet resultSetCount = statement.executeQuery(getOccupierCount);

                        if(resultSetCount.next()){

                            OccupierCount = resultSetCount.getString("Occupier");
                        }
                    }
                } catch (Exception e) {
                }


                Query = "update AnnexRating set Rating = " + Rating + " where Username = '" + passingID + "' and AnnexID = '" + OccupiedPlaceID + "' and Occupier = '" + OccupierCount + "';";
            }
            else{

                String getOccupierCount = "select top 1 * from BoardingRoomRating where Username = '" + passingID + "' order by Occupier desc;";
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {

                        Statement statement = connection.createStatement();
                        ResultSet resultSetCount = statement.executeQuery(getOccupierCount);

                        if(resultSetCount.next()){

                            OccupierCount = resultSetCount.getString("Occupier");
                        }
                    }
                } catch (Exception e) {
                }
                Query = "update BoardingRoomRating set Rating = " + Rating + " where Username = '" + passingID + "' and RoomID = '" + OccupiedPlaceID + "' and Occupier = '" + OccupierCount + "';";
            }

            if (OccupiedPlaceID.trim().equals("")) {

                z = "Please enter valid username and password";

            } else {
                try {
                    connection = connectionclass(User, Pass, DB, IP, PORT);

                    if (connection == null) {
                        z = "turn on cellular data or Wi-Fi";
                    } else {

                        Statement statement = connection.createStatement();

                        statement.executeUpdate(Query);
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