package com.slidepay.resthandler;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by Alex on 8/14/13.
 */
public class LoginHandler extends RestHandler{

    private String mEmail;
    private String mPassword;

    public LoginHandler(String email, String password){
        mEmail = email;
        mPassword = password;

    }

    public boolean login(){
        if(mEmail == null || mPassword == null){
            return false;
        }
        mClient.addHeader("x-cube-email",mEmail);
        mClient.addHeader("x-cube-password",mPassword);
        mEndpoint = "https://supervisor.getcube.com:65532/rest.svc/";
        mResource = "API/endpoint";
        
        performRequest(new AsyncHttpResponseHandler(){
            @Override public void onSuccess(int i , String response){

            }
            @Override public void onFailure(int i , String response){

            }
        });
        return true;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
