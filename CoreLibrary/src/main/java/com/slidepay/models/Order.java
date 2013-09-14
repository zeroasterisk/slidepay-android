package com.slidepay.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 8/14/13.
 */
public class Order {

    public int order_master_id;

    public int company_id;
    public int location_id;
    public int num_items;
    public double gross;
    public double item_discount;
    public double order_discount;
    public double tax;
    public double net;
    public double paid;
    public int user_master_id;
    public String created;
    public String last_update;

    //order detail only
    public double amount_tip;
    public double amount_refunded;

    public ArrayList<Payment> payments;
    public ArrayList<OrderItemEntry> order_item_entry_list;
    public OrderMaster order_master;


    public Order(){

    }

    /**
     *
     * @param orderDetail -
     * @throws JSONException There's a lot of json parsing. When an exception appears, we pass the savings on to you.
     */
    public void updateWithOrderDetails(JSONObject orderDetail) throws JSONException{
        Gson gson = new Gson();
        //the collections!
        //payments first-
        JSONArray jsonPayments = orderDetail.getJSONArray("payment_list");
        Type collectionOfPaymentsType = new TypeToken<ArrayList<Payment>>(){}.getType();
        payments = gson.fromJson(jsonPayments.toString(),collectionOfPaymentsType);

        //the items, which are slightly more complicated.
        JSONArray jsonItems = orderDetail.getJSONArray("order_item_entry_list");
//        JSONArray jsonItemEntries = ;

        //the OrderMaster, which is a 'bag of primitives' case: super simple
        JSONObject jsonOrderMaster = orderDetail.getJSONObject("order_master");
        order_master = gson.fromJson(jsonOrderMaster.toString(),OrderMaster.class);

        //update a few of the primitives
        amount_refunded = orderDetail.getDouble("amount_refunded");
        amount_tip = orderDetail.getDouble("amount_tip");


    }

    @Override public String toString(){
        String string = "";
        string = string + "Order "+order_master_id;
        string = string + "*** gross "+gross;
        string = string + "*** paid "+paid;
        string = string + "*** num payments " + (payments != null ? payments.size() : "null");
        string = string + "*** num items "+(order_item_entry_list != null ? order_item_entry_list.size() : "null");
        string = string + "*** order_master.order_master_id "+(order_master != null ? order_master.order_master_id : "null");
        return string;
    }

//    public void


}
