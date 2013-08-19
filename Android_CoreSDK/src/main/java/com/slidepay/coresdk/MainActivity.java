package com.slidepay.coresdk;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import com.slidepay.models.CurrentSession;
import com.slidepay.resthandler.*;

import org.json.JSONObject;

public class MainActivity extends Activity {

    LoginHandler mHandler;
    OrderHandler mOrders;
    private static final String TAG = "SP_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new LoginHandler("brent@getcube.com","p@ssw0rd");
        mHandler.login(new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                JSONObject jsonResponse = (JSONObject)response;
                Log.d(TAG,"login success. Response: "+jsonResponse);
                getTokenDetails();
            }

            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                //popups
                Log.d(TAG,"login failed. Response: "+response);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void getTokenDetails(){
        mHandler.getUserDetails(new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG,"token detail success!");
                Log.d(TAG,"current session: "+ CurrentSession.getInstance().toString());
                //grab some orders
                getOrders();
            }
            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                Log.d(TAG,"token detail failure!");
            }
        });
    }

    public void getOrders(){
        if(mOrders == null){
            mOrders = new OrderHandler();
        }
        boolean success = mOrders.getSummaries(new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG,"got some orders");
            }
            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                Log.d(TAG,"didn't get those orders");
            }
        });

        if(!success){
            Log.w(TAG,"unable to perform getSummaries request");
        }
    }
    
}
