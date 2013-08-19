package com.slidepay.resthandler;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.slidepay.models.*;

import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Alex on 8/14/13.
 */
public class OrderHandler extends RestHandler{

    SearchFilterArray mSearchParameters;

    /**
     *
     */
    public OrderHandler(){
        mSearchParameters = new SearchFilterArray();
        SearchFilter dateFilter = new SearchFilter();
        dateFilter.field = "last_update";
        dateFilter.condition = "greater_than";
        mSearchParameters.addFilter(dateFilter);
        TAG = "SP_OrderHandler";
        mResource = "report/order_summary";
    }

    /**
     * Load all summaries for all Orders that were created or changed in the last month. An order summary is an Order
     * object that does not have its collection fields populated.
     */
    public boolean getSummaries(ResponseHandler handler){
        SearchFilter filter = mSearchParameters.sfa.getFirst();
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(date);
        gcal.roll(Calendar.DAY_OF_YEAR, -2); //roll back N days
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = dateFormatGmt.format(gcal.getTime());
        filter.value = time;
        Log.d(TAG,"search for all orders since "+time);
        mUserHandler = handler;
        mRequestMethod = RequestMethod.PUT;
        try{
            Gson gson = new Gson();
            String jsonParameters = gson.toJson(mSearchParameters);
            StringEntity stringEntity = new StringEntity(jsonParameters);
            Log.d(TAG,"orders sfa: "+jsonParameters.toString());
            mClient.post(null, getPath(), stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        boolean success = response.getBoolean("success");
                        if (!success) {
                            //shit out a warning to the handler
                            Log.w(TAG, "success is false in PUT report/order_summary" + response);
                            mUserHandler.onFailure(null, response, 0, "PUT report/order_summary returned success = false");
                            return;
                        }
                        //construct an order object
                        ArrayList<Order> orders = processOrderSummaries(response,true);
                        if(orders != null){
                            Log.d(TAG, "orders: " + response.toString());
                            mUserHandler.onSuccess(response);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "orders: " + response.toString());
                        Log.w(TAG, "Unable to parse JSON object", e);
                        mUserHandler.onFailure(e, response, 0, "attempting to retrieve a value from a JSON response threw an exception");
                        return;
                    }
                }

                @Override
                public void onFailure(Throwable e, JSONObject response) {
                    Log.e(TAG, "onFailure", e);
                    Log.e(TAG, "response: " + response.toString());
                    mUserHandler.onFailure(e, response, 0, "Order summary request failed");
                }

            });
            return true;
        }catch(Exception e){
            Log.e(TAG,"unable to create string entity from json object",e);
            return false;
        }
    }

    private ArrayList<Order>processOrderSummaries(JSONObject topLevelResponse, boolean invokeHandlerOnFailure){
        try{
            Log.d(TAG,"trying to process the order summaries");
            JSONObject dataField = topLevelResponse.getJSONObject("data");
            Log.d(TAG,"dataField: "+dataField);
            JSONArray summaryList = dataField.getJSONArray("order_summary_list");
            Log.d(TAG,"summaryList: "+summaryList);
            Gson gson = new Gson();
//            Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
            Type collectionOfOrdersType = new TypeToken<ArrayList<Order>>(){}.getType();
            ArrayList<Order> orders = gson.fromJson(dataField.toString(),collectionOfOrdersType);
            String sanityString = gson.toJson(orders,collectionOfOrdersType);
            Log.d(TAG,"orders sanity check: "+sanityString);
            return orders;
        }catch (JSONException e){
            Log.e(TAG,"topLevelResponse: "+topLevelResponse.toString());
            Log.e(TAG, "JSON exception in processOrderSummaries", e);
            if(invokeHandlerOnFailure){
                mUserHandler.onFailure(e,topLevelResponse,0,"couldn't retrieve data field from JSON response.");
            }
            return null;
        }
    }

    /**
     * Loads all summaries that were created or changed since the reference date
     * @param reference - a reference date as measured in epoch time
     */
    public void getSummaries(int reference){

    }

    /**
     * Performs an order_detail request for each order_master_id in the supplied list. In general,
     * you call this after loading a batch of summaries. For each order_master_id in the list, you get an Order object
     * that has its order_item_entry_list, order_master_item, and payment_list fields populated (in addition to the
     * Date and primitive fields that were populated in the call to getSummaries).
     * @param orderMasterIDs A list of order_master_id'd that correspond to the Order's for which you'd like to load details
     */
    public void getOrderDetail(List<Integer>orderMasterIDs){
    }



}
