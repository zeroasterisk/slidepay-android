package com.slidepay.resthandler;

import com.loopj.android.http.*;

/**
 * Created by Alex on 8/14/13.
 */
public class RestHandler {

    protected static String mEndpoint;
    protected AsyncHttpClient mClient;
    protected String mResource;

    RestHandler(){
        mClient = new AsyncHttpClient();
        mClient.addHeader("x-cube-encoding","application/json");
        mClient.addHeader("Content-Type","application/json");
    }

    protected void performRequest(){

    }

}
