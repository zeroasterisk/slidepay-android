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
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Alex on 8/14/13.
 */
public class OrderHandler extends RestHandler{

    SearchFilterArray mSearchParameters;
    LinkedList<SearchFilter> mSFA;

    /**
     *
     */
    public OrderHandler(){
//        resetReportRequest();
        TAG = "SP_OrderHandler";
    }

    /**
     * Load all summaries for all Orders that were created or changed in the last month. An order summary is an Order
     * object that does not have its collection fields populated.
     */
    public boolean getSummaries(ResponseHandler handler){
        resetSummaryRequest();
        mUserHandler = handler;
        long referenceDate = System.currentTimeMillis()-(60*60*60*24*2*1000);
        return getSummariesSinceReferenceDate(referenceDate,handler);
    }

    private ArrayList<Order>processOrderSummaries(JSONObject topLevelResponse, boolean invokeHandlerOnFailure){
        try{
            Log.d(TAG,"trying to process the order summaries");
            JSONObject dataField = topLevelResponse.getJSONObject("data");
            JSONArray summaryList = dataField.getJSONArray("order_summary_list");
            Gson gson = new Gson();
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




    /*
    Order Detail
    [DataMember] public int? order_master_id { get; set; }
[DataMember] public order_master order_master { get; set; }
[DataMember] public customer customer { get; set; }
[DataMember] public string amount_subtotal { get; set; }
[DataMember] public string amount_tax { get; set; }
[DataMember] public string amount_discount { get; set; }
[DataMember] public string amount_tip { get; set; }
[DataMember] public string amount_total { get; set; }
[DataMember] public string amount_paid { get; set; }
[DataMember] public string amount_refunded { get; set; }
[DataMember] public int item_count { get; set; }
[DataMember] public List<order_item_entry> order_item_entry_list { get; set; }
[DataMember] public List<payment> payment_list { get; set; }
[DataMember] public List<cloud_object> signature_list { get; set; }
     */

    /*

     */

    private Order processOrderDetail(JSONObject topLevelResponse, boolean invokeHandlerOnFailure){
        try{

            Log.d(TAG,"trying to process the order summaries");
            JSONObject dataField = topLevelResponse.getJSONObject("data");
            Log.d(TAG,"dataField: "+dataField);
            JSONArray detailList = dataField.getJSONArray("order_detail_report_detail");
            Log.d(TAG,"summaryList: "+detailList);
            if(detailList.length() < 1){
                return null;
            }
            JSONObject detailJSON = detailList.getJSONObject(0);
            Log.d(TAG,"detail: "+detailJSON);
            Order order = new Order();
//            order.net = detailJSON.getDouble("amount_")
            order.paid = detailJSON.getDouble("amount_paid");
            order.order_master_id = detailJSON.getInt("order_master_id");
            order.num_items = detailJSON.getInt("item_count");



        }catch (JSONException e){
            Log.e(TAG,"topLevelResponse: "+topLevelResponse.toString());
            Log.e(TAG, "JSON exception in processOrderDetail", e);
            if(invokeHandlerOnFailure){
                mUserHandler.onFailure(e,topLevelResponse,0,"couldn't retrieve data field from JSON response.");
            }
            return null;
        }
        return null;
    }


    /**
     ** Loads all a summary for each Order that has been created or changed since the reference date.
     * @param reference - a reference date as measured in epoch time (milliseconds)
     * @param handler - the anonymous callback object used to return the
     * @return
     */
    public boolean getSummariesSinceReferenceDate(long reference, ResponseHandler handler){
        resetSummaryRequest();
        mUserHandler = handler;
        Date date = new Date(reference);
        SearchFilter filter = mSearchParameters.sfa.getFirst();
//        Date date = new Date(System.currentTimeMillis()-(60*60*60*24*2*1000));
//        GregorianCalendar gcal = new GregorianCalendar();
//        gcal.setTime(date);
//        gcal.roll(Calendar.DAY_OF_YEAR, -2); //roll back N days //this will break on the first day of the year
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        //        String time = dateFormatGmt.format(gcal.getTime());
        String time = dateFormatGmt.format(date);
        Log.d(TAG,"Searching for summaries created since "+time);
        filter.value = time;
        Log.d(TAG,"search for all orders since "+time);
        mRequestMethod = RequestMethod.PUT;
        mResource = "report/order_summary";
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
                            mUserHandler.onSuccess(orders);
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

    /**
     * Performs an order_detail request for each order_master_id in the supplied list. In general,
     * you call this after loading a batch of summaries. For each order_master_id in the list, you get an Order object
     * that has its order_item_entry_list, order_master_item, and payment_list fields populated (in addition to the
     * Date and primitive fields that were populated in the call to getSummaries).
     * @param orderMasterIDs A list of order_master_id'd that correspond to the Order's for which you'd like to load details
     */
    public boolean getOrderDetails(List<Integer>orderMasterIDs){
        resetDetailsRequest();
//        mSFA
        return true;
    }

    /**
     * @param orderMasterID The order_master_id for the order object you wish to obtain details for.
     * @param handler The response handler to invoke on completion.
     * @return a sanity check - if the client is misconfigured (no token, haven't logged in), or if the orderMasterID is 0,
     * then this will return false without performing the request. Otherwise, will return true.
     */
    public boolean getOrderDetailForOMID(int orderMasterID, ResponseHandler handler){
        if(orderMasterID < 1 || mToken == null || mToken.equals("")){
            return false;
        }
        resetDetailsRequest();
        SearchFilter omidFilter = mSFA.getFirst();
        omidFilter.value = ""+orderMasterID;
        mRequestMethod = RequestMethod.PUT;
        mResource = "report/order_detail";
        StringEntity stringEntity = null;
        mUserHandler = handler;
        try{
            Gson gson = new Gson();
            String jsonParameters = gson.toJson(mSFA);
            stringEntity = new StringEntity(jsonParameters);
            Log.d(TAG,"orders sfa: "+jsonParameters);
        }catch (Exception e){
            Log.e(TAG,"unable to turn SFA into StringEntity for getOrderDetailForOMID ",e);
            return false;
        }
        //null context
        mClient.put(null, getPath(), stringEntity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (!success) {
                        //shit out a warning to the handler
                        Log.w(TAG, "success is false in PUT report/order_detail" + response);
                        mUserHandler.onFailure(null, response, 0, "PUT report/order_summary returned success = false");
                        return;
                    }
                    //construct an order object
                    Order order = processOrderDetail(response, true);
                    if (order != null) {
                        Log.d(TAG, "orderDetail: " + response.toString());
                        mUserHandler.onSuccess(order);
                    } //the else case is handled in the processOrderDetail call
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
    }

    private void resetSummaryRequest(){
        mSearchParameters = new SearchFilterArray();
        SearchFilter dateFilter = new SearchFilter();
        dateFilter.field = "last_update";
        dateFilter.condition = "greater_than";
        mSearchParameters.addFilter(dateFilter);
    }
    private void resetDetailsRequest(){
        mSFA = new LinkedList<SearchFilter>();
        SearchFilter omidFilter = new SearchFilter();
        omidFilter.field = "order_master_id";
        omidFilter.condition = "equals";
        mSFA.add(omidFilter);
    }

}
