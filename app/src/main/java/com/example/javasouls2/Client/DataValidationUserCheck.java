package com.example.javasouls2.Client;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.javasouls2.DatabaseCredentials;
import com.example.javasouls2.EditAccountWindow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataValidationUserCheck implements DatabaseCredentials {

    private Connection connection;
    private EditText UserE;
    private EditText Password;

    public DataValidationUserCheck(EditText userE, EditText password) {
        UserE = userE;
        Password = password;
    }

    //Student Username and Password Validation Area
    public Boolean GetValidationUsername() throws SQLException {

        String Username = UserE.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        if (connection == null){

        }
        else{

            String query = "SELECT * FROM AppUser WHERE Username = '" + Username + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.next()) {

                UserE.setError("Invalid Username");
                connection.close();
                return false;
            }

            UserE.setEnabled(false);
            return true;
        }

        return false;

    }

    public Boolean GetValidationPassword() throws SQLException {

        String PasswordUser = Password.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "SELECT * FROM AppUser WHERE UserPassword = '" + PasswordUser + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (!resultSet.next()) {

            Password.setError("Password doesn't match");
            connection.close();
            return false;
        }

        Password.setEnabled(false);
        return true;

    }

    //Owner Username and Password Validation Area
    public Boolean GetValidationUsernameOwner() throws SQLException {

        String Username = UserE.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "SELECT * FROM Client WHERE NIC = '" + Username + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (!resultSet.next()) {

            UserE.setError("Invalid NIC Number");
            connection.close();
            return false;
        }

        UserE.setEnabled(false);
        return true;

    }

    public Boolean GetValidationPasswordOwner() throws SQLException {

        String PasswordUser = Password.getText().toString();

        connection = connectionclass(User, Pass, DB, IP, PORT);

        String query = "SELECT * FROM Client WHERE Password1 = '" + PasswordUser + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (!resultSet.next()) {

            Password.setError("Password doesn't match");
            connection.close();
            return false;
        }

        Password.setEnabled(false);
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
