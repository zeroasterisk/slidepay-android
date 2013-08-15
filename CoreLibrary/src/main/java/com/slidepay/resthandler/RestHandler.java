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
    private AsyncHttpResponseHandler _mHandler;

    public RestHandler(){
        mClient = new AsyncHttpClient();
        mClient.addHeader("x-cube-encoding","application/json");
        mClient.addHeader("Content-Type","application/json");
        mClient.addHeader("Accept","application/json");
        mRequestMethod = RequestMethod.GET;
    }

    protected void performRequest(JsonHttpResponseHandler handler){
        switch (mRequestMethod){
            case GET:
                _mHandler = handler;
                mClient.get(this.getPath(),null,handler);
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
        Log.d(TAG,"getPath() = "+mEndpoint+mResource);
        return mEndpoint+mResource;
    }

}
