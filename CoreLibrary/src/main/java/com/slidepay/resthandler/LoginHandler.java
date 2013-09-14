package com.slidepay.resthandler;

import android.util.Log;

import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.slidepay.models.CurrentSession;

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

    /**
     * @param userHandler An anonymous instance of ResponseHandler that implements onSuccess and on Failure. In onSuccess, the response
     *                    object is an instance of JSONObject. There's not much to read there (the token necessary for future API
     *                    calls has been stored in the RestHandler base class), but you're welcome to peak at it.
     * @return if email or password haven't been set, then this will return false.
     */
    public boolean login(ResponseHandler userHandler){
        if(mEmail == null || mPassword == null){
            return false;
        }
        mUserHandler = userHandler;
        mClient.addHeader("x-cube-email",mEmail);
        mClient.addHeader("x-cube-password",mPassword);
        mEndpoint = "https://supervisor.getcube.com:65532/rest.svc/";
        mResource = "API/endpoint";
        mClient.get(getPath(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess, message = " + response.toString());
                processSupervisorResponse(response);
            }

            @Override
            public void onFailure(Throwable e, JSONObject response) {
                Log.e(TAG, "onFailure", e);
                Log.e(TAG, "response: " + response.toString());
                mUserHandler.onFailure(e, response, 0, "unable to retrieve endpoint");
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
                mUserHandler.onFailure(null,response,0,"request for endpoint returned success = false");
                return;
            }
            String endpoint = response.getString("data");
            if(endpoint != null){
                mEndpoint = endpoint;
            }else{
                mEndpoint = "";
            }
            mResource = "login";
            Log.d(TAG,"endpoint obtained and set to "+mEndpoint);
        }catch(JSONException e){
            Log.w(TAG,"Unable to convert string response into JSON object",e);
            mUserHandler.onFailure(e,response,0,"");
            return;
        }
        mClient.get(getPath(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "(login) onSuccess, message = " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    if (!success) {
                        //shit out a warning to the handler
                        Log.w(TAG, "success is false in GET API/login response" + response);
                        mUserHandler.onFailure(null, response, 0, "request for login returned success = false");
                        return;
                    }
                    String token = response.getString("data");
                    mToken = new String(token);
                    mClient.addHeader("x-cube-token",mToken);
                    mUserHandler.onSuccess(response);
                } catch (JSONException e) {
                    Log.w(TAG, "Unable to parse JSON object", e);
                    mUserHandler.onFailure(e, response, 0, "attempting to retrieve a value from a JSON response threw an exception");
                    return;
                }
            }

            @Override
            public void onFailure(Throwable e, JSONObject response) {
                Log.e(TAG, "onFailure", e);
                Log.e(TAG, "response: " + response.toString());
                mUserHandler.onFailure(e, response, 0, "login request failed");
            }
        });
    }

    /**
     * Populates the CurrentSession with the user details appropriate for the email/password combo used to log in. JSON format for the response:
     *
     *{ ..., "data": {
     "company_id": int,
     "is_comgr": boolean,
     "location_name": string,
     "is_clerk": boolean,
     "random": ?,
     "password": null,
     "endpoint": "https://api.getcube.com:65532",
     "is_locmgr": boolean,
     "location_id": int,
     "server_name": string,
     "first_name": string,
     "timezone": "",
     "ip_address": string,
     "is_admin": boolean,
     "company_name": string,
     "email": string,
     "created": String (UTC format - "yyyy-MMM-dd'T'HH:mm:ss",
     "last_name": String,
     "is_isv": boolean,
     "user_master_id": int
     } ...}
     *
     * @return If you've logged in, then this returns true. If you haven't, then no API calls are made, and this returns false.
     */
    public boolean getUserDetails(ResponseHandler handler){
        if(mToken == null || mToken.equals("")){
            return false;
        }
        mUserHandler = handler;
        mResource = "token/detail";
        mRequestMethod = RequestMethod.GET;
        performRequest(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "(login) tokenDetails, message = " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    if (!success) {
                        //shit out a warning to the handler
                        Log.w(TAG, "success is false in GET token/detail response" + response);
                        mUserHandler.onFailure(null, response, 0, "request for token detail returned success = false");
                        return;
                    }
                    configureSessionFromResponse(response.getJSONObject("data"));
                    mUserHandler.onSuccess(response);
                } catch (JSONException e) {
                    Log.w(TAG, "Unable to parse JSON object", e);
                    mUserHandler.onFailure(e, response, 0, "attempting to retrieve a value from a JSON response threw an exception");
                    return;
                }
            }

            @Override
            public void onFailure(Throwable e, JSONObject response) {
                Log.e(TAG, "onFailure", e);
                Log.e(TAG, "response: " + response.toString());
                mUserHandler.onFailure(e, response, 0, "token detail request failed");
            }
        });

        return  true;
    }

    private void configureSessionFromResponse(JSONObject object){
        CurrentSession.getInstance().setSessionFromJSON(object);
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
