package com.example.javasouls2;

public interface ClientCommandInterface {
    //String InsertAllData = "INSERT INTO Client(NIC,Password1,Password2,FirstName,PhoneNumber1,Gender,AddressLine1,City,PostalCode) VALUES ";
    String createAccountpart1 = "INSERT INTO Client(NIC,Password1,FirstName,LastName) VALUES ";
    String createAccountpart2 = "INSERT INTO Client(PhoneNumber1,PhoneNumber2,Gender,Email) VALUES ";
    //String getCreateAccountpart2WithoutEmail = "INSERT INTO Client(PhoneNumber1,PhoneNumber2,Gender,Email) VALUES ";
    String createAccountpart3 = "INSERT INTO Client(AddressLine1,Line2,City,PostalCode) VALUES ";
}
