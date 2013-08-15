package com.slidepay.resthandler;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;

/**
 * Created by Alex on 8/14/13.
 */

public class LoginHandler extends RestHandler{

    private String mEmail;
    private String mPassword;


    public LoginHandler(String email, String password){
        mEmail = email;
        mPassword = password;
        TAG = "SP_LoginHandler";
        Log.d(TAG,"login handler constructor");
    }

    /*
        protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;
     */
    public boolean login(){
        if(mEmail == null || mPassword == null){
            return false;
        }
        mClient.addHeader("x-cube-email",mEmail);
        mClient.addHeader("x-cube-password",mPassword);
        mEndpoint = "https://supervisor.getcube.com:65532/rest.svc/";
        mResource = "API/endpoint";
        performRequest(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess, message = " + response.toString());
                processSupervisorResponse(response);
            }
            @Override
            public void onFailure(Throwable e, JSONObject response) {
                Log.e(TAG, "onFailure", e);
                Log.e(TAG, "response: "+ response.toString());
            }
        });
        return true;
    }

    protected void processSupervisorResponse(JSONObject response){
        try{
            boolean success = response.getBoolean("success");
            if(!success){
                //shit out a warning to the handler
                Log.w(TAG,"success is false in GET API/endpoint response"+response);
                return;
            }
            String endpoint = response.getString("data");
            mEndpoint = new String(endpoint);
            mResource = "login";
            Log.d(TAG,"endpoint obtained and set to "+mEndpoint);
        }catch(JSONException e){
            Log.w(TAG,"Unable to convert string response into JSON object",e);
            return;
        }
        performRequest(new JsonHttpResponseHandler(){
            @Override public void onSuccess(JSONObject response){
                Log.d(TAG,"(login) onSuccess, message = "+response.toString());
                try{
                    boolean success = response.getBoolean("success");
                    if(!success){
                        //shit out a warning to the handler
                        Log.w(TAG,"success is false in GET API/login response"+response);
                        return;
                    }
                    String token = response.getString("data");
                    mToken = new String(token);
                }catch (JSONException e){
                    Log.w(TAG,"Unable to parse JSON object",e);
                    return;
                }
            }
            @Override
            public void onFailure(Throwable e, JSONObject response) {
                Log.e(TAG, "onFailure", e);
                Log.e(TAG, "response: "+ response.toString());
            }
        });
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
