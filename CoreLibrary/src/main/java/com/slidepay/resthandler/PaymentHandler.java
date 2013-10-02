package com.slidepay.resthandler;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.slidepay.models.Payment;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Alex on 9/12/13.
 * Every call to payment overwrites the previous payment response handler. If you care about the response, then you should wait for a request to finish before using the same
 *   PaymentHandler to make another payment. Alternatively, you can manage use multiple instances of PaymentHandler to manage multiple requests.
 *
 * The calls to refund() come with the same caveat.
 *
 * It is safe to have a payment request and refund request running in parallel. The handlers are stored separately.
 *
 * Currently, the Android SDK only supports the payment/simple API. When passing a SimplePayment object to the makeSimplePayment() method, the following fields will need to be present:
 *   company_id, location_id, amount_to_charge, credit
 *
 */
public class PaymentHandler extends RestHandler {

    private ResponseHandler mRefundHandler;
    private Payment mPayment;

    public PaymentHandler(){
        TAG = "SP_PaymentHandler";
    }

    /**
     * @param payment Cannot be null. the payment object describing the transaction.
     * @param handler Can be null - the ResponseHandler to be used when the payment operation has finished. Can be null.
     */
    public void makeSimplePayment(Payment payment, ResponseHandler handler){
        //verify that the payment object has been filled out correctly
        if(handler == null){
            mUserHandler = new ResponseHandler() {
                @Override
                public void onSuccess(Object response) {

                }

                @Override
                public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {

                }
            }; // a blank handler
        }else{
            mUserHandler = handler;
        }
        mPayment = payment;
        String paymentJSON = payment.asJson();
        StringEntity entity;
        try {entity = new StringEntity(paymentJSON);}
        catch (UnsupportedEncodingException e){Log.e(TAG,"unsupported encoding exception");e.printStackTrace();entity = null;};
        Log.d(TAG,"paymentJSON = "+paymentJSON);
        mResource = "payment/simple";
        mClient.post(null,getPath(),entity,"application/json",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                boolean success = mUserHandler.checkResponseForSuccessFlag(jsonObject);
                if(success){
                    try{
                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.d(TAG,"data: "+data);
                        PaymentHandler.this.mPayment.setPaymentID(data.getInt("payment_id"));
                        Log.d(TAG,"paymentID: "+data.getInt("payment_id"));
                        PaymentHandler.this.mPayment.setOrderMasterID(data.getInt("order_master_id"));
                        Log.d(TAG,"omid: "+data.getInt("order_master_id"));
                        mUserHandler.onSuccess(mPayment);
                    }catch (JSONException e){
                        Log.d(TAG,"caught an exception when checking payment response for success");
                        mUserHandler.onFailure(e,jsonObject,0,"");
                    }
                }else{
                    Log.w(TAG,"Payment returned success == false.");
                }
            }
            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                mUserHandler.onFailure(throwable,jsonObject,0,"");
            }
        });
    }

    /**
     * @param paymentID Cannot be null. The payment_id for the payment you'd like to refund (this is different from the order_master_id).
     *                  The field will be present and populated in the Payment object returned by makeSimplePayment().
     * @param handler Can be null - the ResponseHandler to be used when the payment operation has finished.
     */
    public void refund(int paymentID, ResponseHandler handler){

        if(handler == null){
            mRefundHandler = new ResponseHandler() {
                @Override
                public void onSuccess(Object response) {

                }

                @Override
                public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {

                }
            }; //a blank handler
        }else{
            mRefundHandler = handler;
        }
        mResource = "payment/refund/"+paymentID;
        mClient.post(getPath(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                boolean success = mRefundHandler.checkResponseForSuccessFlag(jsonObject);
                if(success){
                    mRefundHandler.onSuccess(null);
                }else{
                    Log.w(TAG,"refund returned success == false.");
                }
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                mRefundHandler.onFailure(throwable,jsonObject,0,"");
            }
        });
    }

    /**
     * As above.
     * @param payment the payment to be refunded. Cannot be null.
     * @param handler as above.
     */
    public void refund(Payment payment, ResponseHandler handler){
        int paymentID = payment.getPaymentID();
        assert (payment != null);
        assert(paymentID > 0);
        refund(payment.getPaymentID(),handler);
    }

    public void find(int paymentID, ResponseHandler handler){

        if(handler == null){
            mRefundHandler = new ResponseHandler() {
                @Override
                public void onSuccess(Object response) {

                }

                @Override
                public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {

                }
            }; //a blank handler
        }else{
            mRefundHandler = handler;
        }
        mResource = "payment";
        mClient.put(getPath(),new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
            }

        });
    }

}
