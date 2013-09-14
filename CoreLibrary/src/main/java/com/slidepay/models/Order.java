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
    /*
    @property (nonatomic, retain) NSSet *orderItemEntryList;
    @property (nonatomic, retain) OrderMasterItem *orderMasterItem;
    @property (nonatomic, retain) NSSet *paymentList;
    @property (nonatomic, retain) NSSet *processorResponseList;
    @property (nonatomic, retain) NSNumber *order_master_id;
    @property (nonatomic, retain) NSNumber *refund;
    @property (nonatomic, retain) NSNumber * company_id;
    @property (nonatomic, retain) NSNumber * location_id;
    @property (nonatomic, retain) NSNumber * num_items;
    @property (nonatomic, retain) NSNumber * gross;
    @property (nonatomic, retain) NSNumber * item_discount;
    @property (nonatomic, retain) NSNumber * order_discount;
    @property (nonatomic, retain) NSNumber * tax;
    @property (nonatomic, retain) NSNumber * net;
    @property (nonatomic, retain) NSNumber * paid;
    @property (nonatomic, retain) NSNumber * user_master_id;
    @property (nonatomic, retain) NSDate   * created;
    @property (nonatomic, retain) NSDate   * last_update;
    */



    /*
        order summary
        {
[DataMember] public int company_id { get; set; }
[DataMember] public string company_name { get; set; }
[DataMember] public int location_id { get; set; }
[DataMember] public string location_name { get; set; }
[DataMember] public int user_master_id { get; set; }
[DataMember] public string first_name { get; set; }
[DataMember] public string last_name { get; set; }
[DataMember] public int order_master_id { get; set; }
[DataMember] public string order_title { get; set; }
[DataMember] public DateTime? created { get; set; }
[DataMember] public string order_state { get; set; }
[DataMember] public int num_items { get; set; }
[DataMember] public string gross { get; set; }
[DataMember] public string item_discount { get; set; }
[DataMember] public string order_discount { get; set; }
[DataMember] public string tax { get; set; }
[DataMember] public string net { get; set; }
[DataMember] public string paid { get; set; }
}
     */

    /*
    Order Detail
    {
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
}
     */

    /*
    common between the two: (names written as <summary_name> -> <detail_name>
    tax -> amount_tax
    paid -> amount_paid
    X -> amount_discount ? item_discount, order_discount?
    X -> amount_tip { get; set; }
    X -> amount_total { get; set; }
    X -> amount_refunded { get; set; }
    num_items->item_count
    order_master_id -> order_master_id
    X -> order_item_entry_list
    X -> payment_list
    X -> signature_list
    X -> order_master
    */


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
