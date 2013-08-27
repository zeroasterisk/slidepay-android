package com.slidepay.models;

/**
 * Created by Alex on 8/21/13.
 */
public class OrderMaster {
    /*
    [DataMember] public int? order_master_id { get; set; }
[DataMember] public int? company_id { get; set; }
[DataMember] public int? location_id { get; set; }
[DataMember] public int? user_master_id { get; set; }
[DataMember] public string first_name { get; set; } // NULLABLE
[DataMember] public string last_name { get; set; } // NULLABLE
[DataMember] public int? customer_id { get; set; }
[DataMember] public int? ticket_number { get; set; } // NULLABLE
[DataMember] public string order_title { get; set; } // NULLABLE
[DataMember] public string order_state { get; set; }
[DataMember] public string kds_state { get; set; } // NULLABLE
[DataMember] public int? order_fulfilled { get; set; }
[DataMember] public decimal? order_discount { get; set; } // NULLABLE
[DataMember] public string comments { get; set; } // NULLABLE
[DataMember] public DateTime? created { get; set; } // NULLABLE
[DataMember] public DateTime? last_update { get; set; } // NULLABLE
     */

    public double order_discount;
    public boolean order_fulfilled;
    public int company_id;
    public int location_Id;
    public int customer_id;
    public int ticket_number;
    public int user_master_id;
    public int order_master_id;
    public String first_name;
    public String last_name;
    public String order_title;
    public boolean order_state;
    public boolean kds_state;
    public String comments;
    public String created;
    public String last_update;

    public OrderMaster(){

    }

}
