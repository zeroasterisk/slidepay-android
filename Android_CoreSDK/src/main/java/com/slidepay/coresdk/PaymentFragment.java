package com.slidepay.coresdk;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.slidepay.models.Payment;
import com.slidepay.ramblersupport.SwipeListener;
import com.slidepay.resthandler.PaymentHandler;
import com.slidepay.resthandler.ResponseHandler;

import net.homeatm.reader.ReaderController;

import org.json.JSONObject;

/**
 * Created by Alex on 9/11/13.
 */
public class PaymentFragment extends Fragment{
    private static String TAG = "SP_PaymentFragment";
    private PaymentHandler mPaymentHandler;
    private SwipeListener mSwipeListener;
    ProgressDialog mPaymentDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.w(TAG, "onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        Log.d(TAG,"inflate the view for the login fragment");
        View v = inflater.inflate(R.layout.fragment_payment,parent,false);
        TextView successLabel = (TextView)v.findViewById(R.id.payment_success_view);
        if(successLabel != null){
            successLabel.setVisibility(0);
        }

        mSwipeListener = new SwipeListener(getActivity().getApplicationContext(),new SwipeListener.SwipeHandler() {
            @Override
            public void swipeObtained(Bundle ccBundle) {
                Log.d(TAG,"creating payment");
                Payment payment = Payment.create(ccBundle,1);
                Log.d(TAG,"performing payment");
                PaymentFragment.this.performPayment(payment);
            }
            @Override
            public void swipeFailed() {
                Log.w(TAG,"swipe failed");
            }
        });
        mSwipeListener.setListening(true);

        return v;
    }

    /**
     * When a card is swiped, this class makes and refunds a $1.00 payment.
     * @param payment a payment object! See that class for details (use the create methods)
     */
    private void performPayment(Payment payment){
        payment.setNotes("android test payment");
        Log.d(TAG,"making payment");
        if(mPaymentHandler == null){
            mPaymentHandler = new PaymentHandler();
        }
        presentProcessingDialog();
        mPaymentHandler.makeSimplePayment(payment,new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG,"performing refund");
                Payment paymentResposnse = (Payment)response;
                if(paymentResposnse == null){
                    Log.w(TAG,"payment object returned by payment/simple is null. "+paymentResposnse);
                    this.onFailure(new NullPointerException(),null,0,"");
                }
                mPaymentHandler.refund(paymentResposnse,new ResponseHandler() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d(TAG,"refund successful");
                        mPaymentDialog.dismiss();
                        presentRefundToast(true);
                        presentPaymentToast(true);
                    }
                    @Override
                    public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                        Log.d(TAG,"refund failed");
                        mPaymentDialog.dismiss();
                        if(e != null){
                            e.printStackTrace();
                        }
                        if (response != null){
                            Log.d(TAG,"refund response = "+response);
                        }
                        presentRefundToast(false);
                    }
                });
            }

            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                Log.d(TAG,"payment failed");
                if(e != null){
                    Log.e(TAG,"caught an exception while processing a payment: "+e.getLocalizedMessage());
                    e.printStackTrace();
                }
                if (response != null){
                    Log.w(TAG,"payment response = "+response);
                }
                mPaymentDialog.dismiss();
                presentPaymentToast(false);
            }
        });
    }
    private void presentProcessingDialog(){
        mPaymentDialog = new ProgressDialog(getActivity());
        mPaymentDialog.setMessage("Processing payment. Please Wait");
        mPaymentDialog.setCancelable(false);
        mPaymentDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mPaymentDialog.show();
    }
    private void presentSwipeToast(boolean success){
        String message;
        if(success){
            message = "Swipe Successful";
        }else{
            message = "Swipe Failed";
        }
        presentCenterToastWithMessage(message,Toast.LENGTH_LONG);
    }
    private void presentPaymentToast(boolean success){
        String message;
        if(success){
            message = "Payment Successful";
        }else{
            message = "Payment Failed";
        }
        presentUpperToastWithMessage(message,Toast.LENGTH_LONG);
    }
    private void presentRefundToast(boolean success){
        String message;
        if(success){
            message = "Refund Successful";
        }else{
            message = "Refund Failed";
        }
        presentCenterToastWithMessage(message,Toast.LENGTH_LONG);
    }
    private void presentCenterToastWithMessage(String string, int duration){
        Toast toast = Toast.makeText(getActivity(),string, duration);
        toast.setGravity(Gravity.CENTER,0,200);
        toast.show();
    }
    private void presentUpperToastWithMessage(String string, int duration){
        Toast toast = Toast.makeText(getActivity(),string, duration);
        toast.setGravity(Gravity.CENTER,0,300);
        toast.show();
    }

}
