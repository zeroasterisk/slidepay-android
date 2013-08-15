package com.slidepay.models;

/**
 * Created by Alex on 8/14/13.
 */
public class CurrentSession {
    private static CurrentSession ourInstance = new CurrentSession();

    protected String firstName;
    protected String lastName;
    protected String email;
    protected String token;

    public static CurrentSession getInstance() {
        return ourInstance;
    }

    private CurrentSession() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
