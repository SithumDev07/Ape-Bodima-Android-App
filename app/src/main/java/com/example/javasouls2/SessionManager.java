package com.example.javasouls2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.security.PublicKey;
import java.util.HashMap;

public class SessionManager {
    //Variables
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    //Session Names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";
    public static final String SESSION_REMEMBERMEUSER = "rememberMeUser";
    public static final String SESSION_REMEMBERME_ANNEX = "rememberMeAnnex";
    public static final String SESSION_REMEMBERME_BOARDING = "rememberMEBoarding";
    public static final String SESSION_REMEMBERME_BOARDING_HOUSE = "rememberMeBoardingHouse";

    //User Session Variables
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_FIRSTNAME = "firstName";

    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_NIC = "NIC";
    public static final String KEY_EMAIL = "emailAddress";
    public static final String KEY_PHONENUMBER = "phoneNumber";
    public static final String KEY_PASSWORD = "passWord";

    //Remember Me variables
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONNIC = "NIC";
    public static final String KEY_SESSIONBOARDINGID = "BoardingID";
    public static final String KEY_SESSIONROOMSCOUNT = "NoOfRooms";
    public static final String KEY_SESSIONCURRENTROOMNO = "Current";
    public static final String KEY_SESSIONPASSWORD = "password";

    private static final String IS_REMEMBERMEUSER ="IsRememberMeUser";
    public static final String KEY_SESSIONUSER = "Username";
    public static final String KEY_SESSIONUSERPASSWORD = "passwordUser";

    public static final String IS_REMEMBERMEANNEX ="IsRememberMeAnnex";
    public static final String KEY_SESSION_ANNEXID = "AnnexID";

    public static final String IS_REMEMBERMEBOARDING = "IsRememberMeBoarding";
    public static final String KEY_SESSION_BOARDINGID = "BoardingID";

    public static final String IS_REMEMBERMEBOARDINGHOUSE = "IsRememberMeBoardingHouse";
    public static final String KEY_SESSION_BOARDING_HOUSEID = "BoardingHouseID";


    //Ad Session
    public static final String AD_SESSIONNIC = "NIC";




    //Constructor
    public SessionManager(Context _context, String sessionName){

        context = _context;
        userSession = context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    /*
    * Users
    * Login Session
    * */

    public void createLoginSession(String firstName,String lastName,String NIC,String email,String phonenumber,String password){
        editor.putBoolean(IS_LOGIN,true);

        editor.putString(KEY_FIRSTNAME,firstName);
        editor.putString(KEY_LASTNAME,lastName);
        editor.putString(KEY_NIC,NIC);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PHONENUMBER,phonenumber);
        editor.putString(KEY_PASSWORD,password);

        editor.commit();
    }

    public HashMap<String,String> getUserDetailsFromSession(){
        HashMap<String,String> userData = new HashMap<>();

        userData.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME,null));
        userData.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME,null));
        userData.put(KEY_NIC, userSession.getString(KEY_NIC,null));
        userData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL,null));
        userData.put(KEY_PHONENUMBER, userSession.getString(KEY_PHONENUMBER,null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD,null));

        return userData;
    }

    public boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN,true)){
            return true;
        }
        else
            return false;
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }

    /*
    *Remember Me
    * Session Functions
    * */

    public void createRememberMenSession(String NIC,String password){
        editor.putBoolean(IS_REMEMBERME,true);

        editor.putString(KEY_SESSIONNIC,NIC);
        editor.putString(KEY_SESSIONPASSWORD,password);

        editor.commit();
    }

    public HashMap<String,String> getRememberMeDetailsFromSession(){
        HashMap<String,String> userData = new HashMap<>();

        userData.put(KEY_SESSIONNIC, userSession.getString(KEY_SESSIONNIC,null));
        userData.put(KEY_SESSIONPASSWORD, userSession.getString(KEY_SESSIONPASSWORD,null));

        return userData;
    }

    public boolean checkRememberme(){
        if(userSession.getBoolean(IS_REMEMBERME,false)){
            return true;
        }
        else
            return false;
    }

    public void createRememberMeSessionUser(String Username,String Password){
        editor.putBoolean(IS_REMEMBERMEUSER,true);

        editor.putString(KEY_SESSIONUSER,Username);
        editor.putString(KEY_SESSIONUSERPASSWORD,Password);

        editor.commit();
    }


    public HashMap<String,String> getRememberMeDetailsUserFromSession(){
        HashMap<String,String> userData = new HashMap<>();

        userData.put(KEY_SESSIONUSER, userSession.getString(KEY_SESSIONUSER,null));
        userData.put(KEY_SESSIONUSERPASSWORD, userSession.getString(KEY_SESSIONUSERPASSWORD,null));

        return userData;
    }

    public boolean checkRemembermeUser(){
        if(userSession.getBoolean(IS_REMEMBERMEUSER,false)){
            return true;
        }
        else
            return false;
    }


    //Annexes

    public void createRememberMeSessionAnnex(String AnnexID){
        editor.putBoolean(IS_REMEMBERMEANNEX,true);

        editor.putString(KEY_SESSION_ANNEXID,AnnexID);

        editor.commit();
    }


    public HashMap<String,String> getRememberMeDetailsAnnexFromSession(){
        HashMap<String,String> AnnexData = new HashMap<>();

        AnnexData.put(KEY_SESSION_ANNEXID, userSession.getString(KEY_SESSION_ANNEXID,null));

        return AnnexData;
    }

    public boolean checkRemembermeAnnex(){
        if(userSession.getBoolean(IS_REMEMBERMEANNEX,false)){
            return true;
        }
        else
            return false;
    }

    //BoardingsRooms

    public void createRememberMeSessionBoarding(String BoardingID){
        editor.putBoolean(IS_REMEMBERMEBOARDING,true);

        editor.putString(KEY_SESSIONBOARDINGID,BoardingID);

        editor.commit();
    }


    public HashMap<String,String> getRememberMeDetailsBoardingFromSession(){
        HashMap<String,String> BoardingData = new HashMap<>();

        BoardingData.put(KEY_SESSIONBOARDINGID, userSession.getString(KEY_SESSIONBOARDINGID,null));

        return BoardingData;
    }

    public boolean checkRemembermeBoardings(){
        if(userSession.getBoolean(IS_REMEMBERMEBOARDING,false)){
            return true;
        }
        else
            return false;
    }


    //Boardings

    public void createRememberMeSessionBoardingHouse(String BoardingID){
        editor.putBoolean(IS_REMEMBERMEBOARDINGHOUSE,true);

        editor.putString(KEY_SESSION_BOARDING_HOUSEID,BoardingID);

        editor.commit();
    }


    public HashMap<String,String> getRememberMeDetailsBoardingHouseFromSession(){
        HashMap<String,String> BoardinghouseData = new HashMap<>();

        BoardinghouseData.put(KEY_SESSION_BOARDING_HOUSEID, userSession.getString(KEY_SESSION_BOARDING_HOUSEID,null));

        return BoardinghouseData;
    }

    public boolean checkRemembermeBoardingsHouse(){
        if(userSession.getBoolean(IS_REMEMBERMEBOARDINGHOUSE,false)){
            return true;
        }
        else
            return false;
    }

}
