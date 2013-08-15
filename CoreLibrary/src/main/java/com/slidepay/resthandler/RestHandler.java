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
    protected String TAG = "RestHandler";
    protected AsyncHttpClient mClient;
    protected String mResource;
    protected RequestMethod mRequestMethod;

    public RestHandler(){
        mClient = new AsyncHttpClient();
        mClient.addHeader("x-cube-encoding","application/json");
        mClient.addHeader("Content-Type","application/json");
        mRequestMethod = RequestMethod.GET;
    }

    protected void performRequest(AsyncHttpResponseHandler handler){
        switch (mRequestMethod){
            case GET:
                mClient.get(this.getPath(),handler);
                break;
            case PUT:
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
        return mEndpoint+mResource;
    }

}
