package com.slidepay.coresdk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import com.slidepay.models.CurrentSession;
import com.slidepay.models.Order;
import com.slidepay.resthandler.*;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    LoginHandler mHandler;
    OrderHandler mOrders;
    private static final String TAG = "SP_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.w(TAG,"On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager manager = getFragmentManager();
        Fragment loginFragment = manager.findFragmentById(R.id.loginContainer);
        if(loginFragment == null){
            loginFragment = new LoginFragment();
            //Fragments are uniquely identified using the parent container view ID.
            //In this case, that's the FrameLayout defined in activity_main.xml
            manager.beginTransaction().add(R.id.loginContainer, loginFragment).commit();
        }
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
                ArrayList<Order> orders = (ArrayList<Order>)response;
                if(orders.size() > 0){
                    Order order = orders.get(0);
                    Log.d(TAG,"found "+orders.size()+" orders, getting details for the first one. "+order.toString());
                    getOrderDetail(order);
                }else{
                    Log.d(TAG,"orders.size() == 0, no details to retrieve");
                }
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

    public void getOrderDetail(Order order){
        int omid = order.order_master_id;
        Log.d(TAG,"getting details for order with OMID = "+omid);
        boolean success = mOrders.getOrderDetailForOMID(omid, new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG,"order detail onSuccess");
                if(response != null){
                    Order order = (Order) response;
                    Log.d(TAG,"got an order detail response " + order.toString());
                }else{
                    Log.d(TAG,"order detail returned null");
                }

            }

            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                Log.w(TAG,"failed to get order detail");
            }
        });
        if(!success){
            Log.w(TAG,"getOrderDetailForOMID(...) returned false");
        }
    }
    
}
