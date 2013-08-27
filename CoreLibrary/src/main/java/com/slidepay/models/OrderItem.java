package com.slidepay.models;

/**
 * Created by Alex on 8/21/13.
 */
public class OrderItem {

    public int order_item_id;
    public int company_id;
    public int location_id;
    public int order_master_id;
    public int user_master_id; // NULLABLE
    public String first_name; // NULLABLE
    public String last_name; // NULLABLE
    public int item_id;
    public int item_group_id;
    public int item_category_id;
    public String item_category_name;
    public int is_child;
    public int parent_order_item_id;
    public String kds_state; // NULLABLE
    public String item_name;
    public int quantity;
    public double item_price;
    public double item_cost;
    public int is_comp; // NULLABLE
    public int is_void; // NULLABLE
    public String comp_reason; // NULLABLE
    public String void_reason; // NULLABLE
    public int tax_included; // NULLABLE
    public int tax_rule_id_1; // NULLABLE
    public String tax_rule_id_1_name; // NULLABLE
    public double tax_rule_id_1_percent; // NULLABLE
    public double tax_rule_id_1_amount; // NULLABLE
    public double tax_rule_id_1_total; // NULLABLE
    public int tax_rule_id_2; // NULLABLE
    public String tax_rule_id_2_name; // NULLABLE
    public double tax_rule_id_2_percent; // NULLABLE
    public double tax_rule_id_2_amount; // NULLABLE
    public double tax_rule_id_2_total; // NULLABLE
    public int tax_rule_id_3; // NULLABLE
    public String tax_rule_id_3_name; // NULLABLE
    public double tax_rule_id_3_percent; // NULLABLE
    public double tax_rule_id_3_amount; // NULLABLE
    public double tax_rule_id_3_total; // NULLABLE
    public int tax_rule_id_4; // NULLABLE
    public String tax_rule_id_4_name; // NULLABLE
    public double tax_rule_id_4_percent; // NULLABLE
    public double tax_rule_id_4_amount; // NULLABLE
    public double tax_rule_id_4_total; // NULLABLE
    public int tax_rule_id_5; // NULLABLE
    public String tax_rule_id_5_name; // NULLABLE
    public double tax_rule_id_5_percent; // NULLABLE
    public double tax_rule_id_5_amount; // NULLABLE
    public double tax_rule_id_5_total; // NULLABLE
    public int tax_rule_id_6; // NULLABLE
    public String tax_rule_id_6_name; // NULLABLE
    public double tax_rule_id_6_percent; // NULLABLE
    public double tax_rule_id_6_amount; // NULLABLE
    public double tax_rule_id_6_total; // NULLABLE
    public int tax_rule_id_7; // NULLABLE
    public String tax_rule_id_7_name; // NULLABLE
    public double tax_rule_id_7_percent; // NULLABLE
    public double tax_rule_id_7_amount; // NULLABLE
    public double tax_rule_id_7_total; // NULLABLE
    public int tax_rule_id_8; // NULLABLE
    public String tax_rule_id_8_name; // NULLABLE
    public double tax_rule_id_8_percent; // NULLABLE
    public double tax_rule_id_8_amount; // NULLABLE
    public double tax_rule_id_8_total; // NULLABLE
    public double tax_amount;
    public double line_amount;
    public double line_discount;
    public double line_total;
    public String comments;
    public int active;
    public String created; // NULLABLE
    public String last_update; // NULLABLE


    public OrderItem(){

    }


}
