package com.example.javasouls2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientCreateAccount extends AppCompatActivity implements ClientCommandInterface, DatabaseCredentials{

    //Variables
    private EditText firstname;
    private EditText lastname;
    private EditText NIC;
    private EditText password1;
    private EditText password2;

    private ImageView backBtn;
    private TextView Title;
    private Button clientNext,LogBack;

    private Connection connection;


    public static final String EXTRA_TEXT = "com.example.javasouls2.ClientCreateAccount.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_create_account);

        firstname = (EditText) findViewById(R.id.FirstName);
        lastname = (EditText) findViewById(R.id.LastName);
        NIC = (EditText) findViewById(R.id.NICNumber);
        password1 = (EditText) findViewById(R.id.Password);
        password2 = (EditText) findViewById(R.id.ReenteredPassword);

        clientNext = (Button) findViewById(R.id.signupNextButton);
        LogBack = (Button) findViewById(R.id.BackToLoginButton);

        backBtn = (ImageView) findViewById(R.id.create_Acc_backButton);
        Title = (TextView) findViewById(R.id.CreateAccTextTitle);

    }

    public void CallNextWindow(View view) throws SQLException {

        InputValidationClient inputValidation = new InputValidationClient(firstname,lastname,NIC,password1,password2);
        DataValidationClient dataValidation = new DataValidationClient(NIC);

        if(!inputValidation.validFirstName() | !inputValidation.validNIC() | !inputValidation.validPassword() | !inputValidation.validReenteredPassword() | dataValidation.GetValidation()){

            return;
        }
        else {
            CheckLogin checkLogin = new CheckLogin();
            checkLogin.execute("");
        }
    }

    public void callLogin(View view){
        Intent intent = new Intent(getApplicationContext(),ClientLoginWindow.class);

        Pair[] pairs = new Pair[1];

        //Since we don't need any use of Client Area Button
        pairs[0] = new Pair<View,String>(findViewById(R.id.BackToLoginButton),"transition_client");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreateAccount.this,pairs);

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
            Toast.makeText(ClientCreateAccount.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(ClientCreateAccount.this , "Login Successful" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ClientCreatePt2.class);

                //Animation

                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(backBtn,"animation_back_btn");
                pairs[1] = new Pair<View,String>(clientNext,"animation_next_btn");
                pairs[2] = new Pair<View,String>(LogBack,"transition_client");
                pairs[3] = new Pair<View,String>(Title,"animation_title_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ClientCreateAccount.this,pairs);
                String passingID = NIC.getText().toString();
                startActivity(intent,options.toBundle());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String clientId = NIC.getText().toString();
            String First = firstname.getText().toString();
            String Last = lastname.getText().toString();
            String Pass1 = password1.getText().toString();
            String Pass2 = password2.getText().toString();

            if(!Pass1.equals(Pass2)){
                z = "Passwords are not match";
            }
            else{

            SessionManager sessionManager = new SessionManager(ClientCreateAccount.this,SessionManager.SESSION_REMEMBERME);
            sessionManager.createRememberMenSession(clientId,Pass1);

            if(clientId.trim().equals("") || First.trim().equals("")){

                z = "Please enter valid username and password";

            }
            else{
                try {
                    connection = connectionclass(User,Pass,DB,IP,PORT);

                    if(connection == null){
                        z = "turn on cellular data or Wi-Fi";
                    }
                    else{
                        String query = "insert into Client(NIC,Password1,FirstName,LastName) values ('" + clientId + "','" + Pass1 + "','" + First + "','" + Last + "');";
                        Statement statement = connection.createStatement();

                        statement.executeUpdate(query);
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