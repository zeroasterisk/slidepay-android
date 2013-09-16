package com.slidepay.resthandler;

import android.util.Log;

import com.loopj.android.http.*;

/**
 * Created by Alex on 8/14/13.
 */
public class RestHandler {
    public enum RequestMethod{
        GET,POST,PUT,DELETE
    }
    protected static String mEndpoint;
    protected static String mToken;
    protected String TAG = "SP_RestHandler";
    protected AsyncHttpClient mClient;
    protected String mResource;
    protected RequestMethod mRequestMethod;
    protected ResponseHandler mUserHandler;

    public RestHandler(){
        mClient = new AsyncHttpClient();
        mClient.addHeader("x-cube-encoding","application/json");
        mClient.addHeader("Content-Type","application/json");
        mClient.addHeader("Accept","application/json");
        if(mToken != null && !mToken.equals("")){
            mClient.addHeader("x-cube-token",mToken);
        }
        mRequestMethod = RequestMethod.GET;
    }
    protected void performRequest(JsonHttpResponseHandler handler){
        switch (mRequestMethod){
            case GET:
                mClient.get(this.getPath(),null,handler);
                break;
            case PUT:
//                mClient
                break;
            case POST:
                break;
        }
    }
    protected String getPath(){
        if(mEndpoint == null){
            Log.w(TAG,"mEndpoint is null - can't get path. returning the empty string");
            return "";
        }
        if(mResource == null){
            Log.w(TAG,"mResource is null - ignoring it and returning mEndpoint as the full path");
            return mEndpoint;
        }
        Log.d(TAG,"getPath() = "+mEndpoint+mResource);
        return mEndpoint+mResource;
    }

}
