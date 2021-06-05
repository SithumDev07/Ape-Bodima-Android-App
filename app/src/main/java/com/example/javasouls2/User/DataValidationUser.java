package com.example.javasouls2.User;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;

import com.example.javasouls2.DatabaseCredentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataValidationUser implements DatabaseCredentials {

    private EditText Username;

    private EditText Phone;
    private EditText Email;

    private Connection connection;

    public DataValidationUser(EditText username) {
        Username = username;
    }


    public DataValidationUser(EditText phone, EditText email) {
        Phone = phone;
        Email = email;
    }

    public Boolean GetValidation() throws SQLException {
        String user = Username.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "SELECT * FROM AppUser WHERE Username= '" + user + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {

            Username.setError("Username is already exist, Please try another");
            connection.close();
            return true;
        }

        Username.setEnabled(false);
        return false;
    }

    public Boolean GetValidationPhone() throws SQLException {

        String PhoneNumber = Phone.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "SELECT * FROM AppUser WHERE PhoneNumber = '" + PhoneNumber + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {

            //Phone.setEnabled(false);
            Phone.setError("This phone number is already taken, Please try another");
            connection.close();
            return true;
        }

        Phone.setEnabled(false);
        return false;

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
