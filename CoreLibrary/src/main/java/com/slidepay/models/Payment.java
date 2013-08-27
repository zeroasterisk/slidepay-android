package com.slidepay.models;

/**
 * Created by Alex on 8/21/13.
 */
public class Payment {



public int payment_id;
public int order_master_id;
public int company_id;
public int location_id;
public int customer_id; // NULLABLE
public int user_master_id;
public String method;
public String method_other; // NULLABLE
public String stored_payment_guid; // NULLABLE
public String cc_type; // NULLABLE
public int cc_present; // NULLABLE
public String cc_number; // NULLABLE
public String cc_expiry_month; // NULLABLE
public String cc_expiry_year; // NULLABLE
public String cc_name_on_card; // NULLABLE
public String cc_address_1; // NULLABLE
public String cc_address_2; // NULLABLE
public String cc_address_3; // NULLABLE
public String cc_city; // NULLABLE
public String cc_state; // NULLABLE
public String cc_billing_zip; // NULLABLE
public String cc_country; // NULLABLE
public String cc_track1data; // NULLABLE
public String cc_track2data; // NULLABLE
public String cc_cvv2; // NULLABLE
public String cc_redacted_number; // NULLABLE
public String encryption_vendor; // NULLABLE
public String encryption_ksn; // NULLABLE
public String encryption_device_serial; // NULLABLE
public double amount;
public double tip_amount; // NULLABLE
public int is_refund; // NULLABLE
public int refund_payment_id; // NULLABLE
public int under_review; // NULLABLE
public String notes; // NULLABLE
public int signature_cloud_object_id; // NULLABLE
public String provider; // NULLABLE
public String provider_payment_token; // NULLABLE
public String provider_transaction_token; // NULLABLE
public String provider_is_approved; // NULLABLE
public String provider_status_code; // NULLABLE
public String provider_status_message; // NULLABLE
public String provider_transaction_state; // NULLABLE
public String provider_approval_code; // NULLABLE
public String provider_capture_state; // NULLABLE
public String provider_capture_status_message; // NULLABLE
public String settlement_transaction_token; // NULLABLE
public double provider_fee_amount; // NULLABLE
public String vendor_id; // NULLABLE
public double vendor_markup_percent; // NULLABLE
public double vendor_markup_pertrans; // NULLABLE
public double fee_percentage; // NULLABLE
public double fee_percentage_amount; // NULLABLE
public double fee_per_transaction; // NULLABLE
public double fee_total; // NULLABLE
public String ip_address; // NULLABLE
public String latitude; // NULLABLE
public String longitude; // NULLABLE
public String country; // NULLABLE
public double distance_from_location; // NULLABLE
public double processor_time_ms; // NULLABLE
public double total_time_ms; // NULLABLE
public String capture_requested; // NULLABLE
public String capture_confirmed; // NULLABLE
public String created; // NULLABLE
public String last_update; // NULLABLE


    public Payment(){

    }







}
