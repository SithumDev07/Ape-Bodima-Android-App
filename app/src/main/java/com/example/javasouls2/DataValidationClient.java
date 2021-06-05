package com.example.javasouls2;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataValidationClient implements DatabaseCredentials {

    private EditText NIC;

    private EditText PhoneNumber;
    private EditText Email;



    Connection connection;

    public DataValidationClient(EditText NIC) {
        this.NIC = NIC;
    }

    public DataValidationClient(EditText phoneNumber, EditText email) {
        PhoneNumber = phoneNumber;
        Email = email;
    }

    public Boolean GetValidation() throws SQLException {
        String z = "";
        String user = NIC.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "SELECT * FROM Client WHERE NIC= '" + user + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {

            NIC.setError("NIC number is already exist, Please Login");
            connection.close();
            return true;
        }

        NIC.setEnabled(false);
        return false;
    }


    public Boolean GetValidationPhoneNumber() throws SQLException {
        String z = "";
        String Phone = PhoneNumber.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "select * from ClientPhoneNumber where PhoneNumber = '" + Phone + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {

            PhoneNumber.setError("This phone number is already taken, Please Login");
            connection.close();
            return true;
        }

        PhoneNumber.setEnabled(false);
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
