package com.slidepay.models;

import java.util.List;

/**
 * Created by Alex on 8/21/13.
 */



public class OrderItemEntry {

    /*
     * [DataContract] public class order_item_entry
     {
     [DataMember] public order_item order_item { get; set; }
     [DataMember] public List<order_item_attr> order_item_attr_list { get; set; }
     [DataMember] public List<order_item_discount> order_item_discount_list { get; set; }
     }
     */
    public OrderItemEntry order_item;
    private List<Object> order_item_attr_list;
    private List<Object> order_item_discount_list;


}
