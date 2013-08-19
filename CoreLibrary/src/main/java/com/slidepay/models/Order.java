package com.slidepay.models;

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
    public int order_master_id;
    public int refund;
    public int company_id;
    public int location_id;
    public int num_items;
    public int gross;
    public int item_discount;
    public int order_discount;
    public int tax;
    public int net;
    public int paid;
    public int user_master_id;
    public String created;
    public String last_update;

    public Order(){

    }
}
