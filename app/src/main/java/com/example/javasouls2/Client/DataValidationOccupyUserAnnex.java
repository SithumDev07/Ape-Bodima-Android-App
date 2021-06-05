package com.example.javasouls2.Client;

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

public class DataValidationOccupyUserAnnex implements DatabaseCredentials {


    private EditText Username;
    private Connection connection;

    public DataValidationOccupyUserAnnex(EditText username) {
        Username = username;
    }


    public Boolean GetValidationUsername() throws SQLException {

        String User = Username.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "SELECT * FROM AppUser WHERE Username = '" + User + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        String queryAnnexCheck = "select * from UserAnnex where Username = '" + User + "';";
        Statement statementAnnexCheck = connection.createStatement();
        ResultSet resultSetAnnexCheck = statementAnnexCheck.executeQuery(queryAnnexCheck);

        String queryBoardingCheck = "select * from UserBoardingHouseRooms where Username = '" + User + "';";
        Statement statementBoardingCheck = connection.createStatement();
        ResultSet resultSetBoardingCheck = statementBoardingCheck.executeQuery(queryBoardingCheck);

        if (!resultSet.next()) {

            Username.setError("Invalid Username");
            connection.close();
            return false;
        }
        else if(resultSetAnnexCheck.next()){

            Username.setError("User is already occupied in an annex");
            connection.close();
            return false;

        }
        else if(resultSetBoardingCheck.next()){

            Username.setError("User is already occupied in a boarding");
            connection.close();
            return false;
        }

        Username.setEnabled(false);
        return true;

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
