package com.example.javasouls2;

import android.widget.EditText;

public class InputValidationClient {

    private EditText firstName;
    private EditText lastName;
    private EditText NIC;
    private EditText password;
    private EditText reenteredPassword;

    private EditText emailAddress;
    //private EditText phoneNumber;
    //private EditText phoneNumberSecondary;

    public InputValidationClient(EditText emailAddress) {
        this.emailAddress = emailAddress;
    }

    public InputValidationClient(EditText firstName, EditText lastName, EditText NIC, EditText password, EditText reenteredPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.NIC = NIC;
        this.password = password;
        this.reenteredPassword = reenteredPassword;
    }

    public InputValidationClient(EditText NIC, EditText password) {
        this.NIC = NIC;
        this.password = password;
    }

    public Boolean validFirstName(){

        String val = firstName.getText().toString();

        if(val.isEmpty()){
            firstName.setError("Field cannot be empty");
            return false;
        }
        else{
            firstName.setError(null);
            firstName.setEnabled(false);
            return true;
        }

    }

    public Boolean validLastName(){
        String val = lastName.getText().toString();

        if(val.isEmpty()){
            lastName.setError("Field cannot be empty");
            return false;
        }
        else{
            lastName.setError(null);
            lastName.setEnabled(false);
            return true;
        }
    }

    public Boolean validNIC(){

        String val = NIC.getText().toString();

        if(val.isEmpty()){
            NIC.setError("Field cannot be empty");
            return false;
        }
        else if(val.length() > 12){
            NIC.setError("NIC number is invalid");
            return false;
        }
        else{
            return true;
        }

    }

    public Boolean validPassword(){
        String val = password.getText().toString();
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
            password.setError("Password cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            password.setError("Password is too week");
            return false;
        }
        else{
            password.setError(null);
            password.setEnabled(false);
            return true;
        }
    }


    public Boolean validReenteredPassword(){
        String val = reenteredPassword.getText().toString();
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
            reenteredPassword.setError("Password cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            reenteredPassword.setError("Password is too week");
            return false;
        }
        else{
            reenteredPassword.setError(null);
            reenteredPassword.setEnabled(false);
            return true;
        }
    }


    public Boolean validEmail(){
        String val = emailAddress.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            return true;
        }
        else if(!val.matches(emailPattern)){
            emailAddress.setError("Email address is not valid");
            return false;
        }
        else{
            emailAddress.setError(null);
            emailAddress.setEnabled(false);
            return true;
        }
    }


    public Boolean validPhoneNumber(EditText PhoneNumber){
        String val = PhoneNumber.getText().toString();

        if(val.isEmpty()){
            PhoneNumber.setError("Phone number cannot be empty");
            return false;
        }
        else{
            PhoneNumber.setError(null);
            //PhoneNumber.setEnabled(false);
            return true;
        }
    }
}
