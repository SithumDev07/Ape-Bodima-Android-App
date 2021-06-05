package com.example.javasouls2.User;

import android.widget.EditText;

public class InputValidationUser {

    private EditText Username;
    private EditText FirstName;
    private EditText LatName;
    private EditText Password;

    private EditText PhoneNumber;
    private EditText EmailAddress;

    public InputValidationUser(EditText username, EditText firstName, EditText latName, EditText password) {
        Username = username;
        FirstName = firstName;
        LatName = latName;
        Password = password;
    }

    public InputValidationUser(EditText phoneNumber, EditText emailAddress) {
        PhoneNumber = phoneNumber;
        EmailAddress = emailAddress;
    }

    public Boolean validFirstName(){

        String val = FirstName.getText().toString();

        if(val.isEmpty()){
            FirstName.setError("Field cannot be empty");
            return false;
        }
        else{
            FirstName.setError(null);
            FirstName.setEnabled(false);
            return true;
        }

    }

    public Boolean validLastName(){
        String val = LatName.getText().toString();

        if(val.isEmpty()){
            LatName.setError("Field cannot be empty");
            return false;
        }
        else{
            LatName.setError(null);
            LatName.setEnabled(false);
            return true;
        }
    }


    public Boolean validUsername(){

        String val = Username.getText().toString();

        if(val.isEmpty()){
            Username.setError("Field cannot be empty");
            return false;
        }
        else{
            return true;
        }

    }


    public Boolean validPassword(){
        String val = Password.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                //"(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                //"(?=\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if(val.isEmpty()){
            Password.setError("Password cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            Password.setError("Password is too week");
            return false;
        }
        else{
            Password.setError(null);
            Password.setEnabled(false);
            return true;
        }
    }



    public Boolean validEmail(){
        String val = EmailAddress.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            return true;
        }
        else if(!val.matches(emailPattern)){
            EmailAddress.setError("Email address is not valid");
            return false;
        }
        else{
            EmailAddress.setError(null);
            EmailAddress.setEnabled(false);
            return true;
        }
    }

    public Boolean validPhoneNumber(EditText PhoneNumber){
        String val = PhoneNumber.getText().toString();

        String PhoneNumberLength = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                //"(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                //"(?=\S+$)" +           //no white spaces
                ".{10,}" +               //at least 10 characters
                "$";

        if(val.isEmpty()){
            PhoneNumber.setError("Phone number cannot be empty");
            return false;
        }
        else if(!val.matches(PhoneNumberLength)){
            PhoneNumber.setError("Phone number is not valid");
            return false;
        }
        else{
            PhoneNumber.setError(null);
            //PhoneNumber.setEnabled(false);
            return true;
        }
    }
}
