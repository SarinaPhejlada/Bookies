package com.example.bookies;

import androidx.annotation.Nullable;

public class UserAccount {

    private String fistName
            ,lastName
            ,emailAddress
            ,password;

    //default constructor
    public UserAccount() {
        this.fistName = "None";
        this.lastName = "None";
        this.emailAddress = "None";
        this.password = "Not set";
    }

    public UserAccount(String fistName, String lastName, String emailAddress, String password) {
        this.fistName = fistName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "fistName='" + fistName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        UserAccount account = (UserAccount) obj;
        return account.fistName == this.fistName
                && account.emailAddress == this.emailAddress
                && account.lastName == this.lastName
                && account.password == this.password;
    }
}
