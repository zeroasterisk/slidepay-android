package com.slidepay.models;

import android.os.Bundle;

import com.google.gson.Gson;
import com.slidepay.corelibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alex on 8/21/13.
 */
public abstract class Payment {

    protected String method;
    protected String latitude;
    protected String longitude;
    protected double amount;
    protected String notes;
    protected int payment_id;
    protected int order_master_id;

    public static Payment create(String ccNumber, String expMonth, String expYear, String cvv2, String zip, double amount){
        SimpleKeyedPayment payment = new SimpleKeyedPayment();
        payment.cc_number = ccNumber;
        payment.cc_expiry_month = expMonth;
        payment.cc_expiry_year  = expYear;
        payment.cc_cvv2 = cvv2;
        payment.cc_billing_zip = zip;
        payment.amount = amount;
        payment.cc_type = typeFromCCNumber(ccNumber);
        return payment;
    }
    public static Payment create(Bundle swipeBundle,double amount){
        SimpleSwipedPayment payment = new SimpleSwipedPayment();
        payment.encryption_ksn = swipeBundle.getString("ksn");
        payment.encryption_vendor = swipeBundle.getString("vendor");
        payment.cc_track2data = swipeBundle.getString("track2");
        payment.amount = amount;
        return payment;
    }

    public static String typeFromCCNumber(String ccNumber){
        ccNumber = ccNumber.replaceAll("\\s+","");
        String visaPattern = "^4[0-9]{12}(?:[0-9]{3})?$";
        String amexPattern = "^3[47][0-9]{13}$";
        String discPattern = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String masterPattern = "^5[1-5][0-9]{14}$";
        if(ccNumber.matches(visaPattern)){
            return "Visa";
        }else if(ccNumber.matches(amexPattern)){
            return "MasterCard";
        }else if(ccNumber.matches(discPattern)){
            return "AmericanExpress";
        }else if(ccNumber.matches(masterPattern)){
            return "Discover";
        }else{
            return null;
        }



    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    public String getNotes(){
        return this.notes;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }
    public double getAmount(){
        return this.amount;
    }
//    boolean validate(){
//        return true;
//    }
    public String asJson() {
        Gson gson = new Gson();
        String selfJSON = gson.toJson(this);
        return selfJSON;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public int getPaymentID() {
        return payment_id;
    }
    public void setPaymentID(int paymentID) {
        this.payment_id = paymentID;
    }
    public int getOrderMasterID() {
        return order_master_id;
    }
    public void setOrderMasterID(int orderMasterID) {
        this.order_master_id = orderMasterID;
    }
}

/**
 * Bags-o-primitives
 */
abstract class SimpleCCPayment extends Payment{
    SimpleCCPayment(){
        method = "CreditCard";
    }
}
class SimpleKeyedPayment extends SimpleCCPayment{
    protected String cc_expiry_month;
    protected String cc_expiry_year;
    protected String cc_number;
    protected String cc_cvv2;
    protected String cc_billing_zip;
    protected String cc_type;
}
class SimpleSwipedPayment extends SimpleCCPayment{
    String cc_track2data;
    String encryption_vendor;
    String encryption_ksn;
}