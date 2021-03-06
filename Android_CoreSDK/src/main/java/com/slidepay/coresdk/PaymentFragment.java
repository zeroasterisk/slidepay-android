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
        Log.d(TAG,"inflate the view for the payment fragment");
        View v = inflater.inflate(R.layout.fragment_payment,parent,false);
        TextView successLabel = (TextView)v.findViewById(R.id.payment_success_view);
        if(successLabel != null){
            successLabel.setVisibility(0);
        }
        startListening();
//        mSwipeListener.mReaderController.stopReader();
        return v;
    }

    @Override
    public void onPause() {
        Log.d(TAG,"on pause");
        if(mSwipeListener != null){
//            mSwipeListener.mReaderController.getReaderState()
            mSwipeListener.stopListening();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG,"on resume");
        if(mSwipeListener == null){
            startListening();
        }else{
            mSwipeListener.setListening(true);
        }
        super.onResume();
    }

    private void startListening(){
        mSwipeListener = new SwipeListener(getActivity().getApplicationContext(),new SwipeListener.SwipeHandler() {
            @Override
            public void swipeObtained(Bundle ccBundle) {
                Log.d(TAG,"creating payment");
                Payment payment = Payment.create(ccBundle,1);
                Log.d(TAG,"performing payment");
//                PaymentFragment.this.performPayment(payment);
                Log.d(TAG,"reader state = "+PaymentFragment.this.mSwipeListener.mReaderController.getReaderState());
                PaymentFragment.this.mSwipeListener.mReaderController.startReader();
            }
            @Override
            public void swipeFailed(int code) {
                Log.w(TAG,"swipe failed with code "+code);
                Log.d(TAG,"reader state = "+PaymentFragment.this.mSwipeListener.mReaderController.getReaderState());
                //mSwipeListener.mReaderController.startReader();
                if(PaymentFragment.this.mSwipeListener.mReaderController.isDevicePresent()){
                    mSwipeListener.mReaderController.startReader();
                }
            }

            @Override
            public void stateChanged(int newState) {
                Log.d(TAG,"Rambler state changed "+newState);
                if(newState == SwipeListener.DEVICE_CONNECTED){
                    Log.d(TAG,"Device connected");
                    if(mSwipeListener != null){
                        mSwipeListener.setListening(true);
                    }else{
                        startListening();
                    }
                }else if (newState == SwipeListener.DEVICE_DISCONNECTED){
                    Log.d(TAG,"Device disconnected");
                    if(mSwipeListener != null){
                        mSwipeListener.stopListening();
                    }else{

                    }
                }else if (newState == SwipeListener.DEVICE_WAITING){
                    Log.d(TAG,"Device device waiting for swipe");
                }else if (newState == SwipeListener.DEVICE_IDLE){
                    Log.d(TAG,"Device idle");
                }else if (newState == SwipeListener.DEVICE_DECODING){
                    Log.d(TAG,"Device decoding");
                }else if (newState == SwipeListener.DEVICE_RECORDING){
                    Log.d(TAG,"Device recording");
                }

            }
        });
//        mSwipeListener.setListening(true);


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
                        presentPaymentToast(true);
                        presentRefundToast(true);
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
        presentUpperToastWithMessage(message,Toast.LENGTH_SHORT);
    }
    private void presentRefundToast(boolean success){
        String message;
        if(success){
            message = "Refund Successful";
        }else{
            message = "Refund Failed";
        }
        presentCenterToastWithMessage(message,Toast.LENGTH_SHORT);
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
    private void presentAlertWithMessage(String message){

    }

}
