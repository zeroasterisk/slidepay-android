package com.slidepay.ramblersupport;


/**
 * Created by Alex on 9/11/13.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import net.homeatm.reader.ReaderController;

import java.util.HashMap;

public class SwipeListener {

    private static final String TAG = "SP_SwipeListener";
    public ReaderController mReaderController;
    private boolean isListening;
    private SwipeHandler mUserHandler;

    public SwipeListener(Context context, SwipeHandler handler){
        isListening = true;
        mUserHandler = handler;
        mReaderController = new ReaderController(context,new ReaderController.ReaderStateChangedListener() {
            @Override
            public void onGetKsnCompleted(String s) {
                Log.d(TAG,"onGetKsnCompleted: "+s);
            }

            @Override
            public void onCardSwipeDetected() {
                Log.d(TAG,"swipe detected!");
            }

            @Override
            public void onDecodeCompleted(HashMap<String, String> stringStringHashMap) {
                Log.d(TAG,"onDecodeCompleted: "+stringStringHashMap.toString());
                String ksn = stringStringHashMap.get("ksn");
                String track2 = stringStringHashMap.get("encTracks");
                Bundle bundle = new Bundle();
                bundle.putString("ksn",ksn); //ksn
                bundle.putString("vendor","rambler"); //vendor
                bundle.putString("track2",track2);
                mUserHandler.swipeObtained(bundle);
            }

            @Override
            public void onDecodeError(ReaderController.DecodeResult decodeResult) {
                Log.w(TAG,"decode error");
                mUserHandler.swipeFailed();
            }

            @Override
            public void onError(String s) {
                Log.w(TAG,"onError");
                mUserHandler.swipeFailed();
            }

            @Override
            public void onInterrupted() {
                Log.w(TAG,"onInterrupted");
                if(SwipeListener.this.isListening){
                    SwipeListener.this.setListening(false);
                }
                SwipeListener.this.setListening(true);
            }

            @Override
            public void onNoDeviceDetected() {

            }

            @Override
            public void onTimeout() {
                Log.w(TAG,"onTimeout");
                mUserHandler.swipeFailed();
            }

            @Override
            public void onDecodingStart() {

            }

            @Override
            public void onWaitingForCardSwipe() {

            }

            @Override
            public void onWaitingForDevice() {

            }

            @Override
            public void onDevicePlugged() {

            }

            @Override
            public void onDeviceUnplugged() {

            }
        });
    }
    private SwipeListener(){}

    public boolean isListening() {
        return isListening;
    }
    public void setListening(boolean listening){
        isListening = listening;
        if(isListening){
            mReaderController.startReader();
        }else{
            mReaderController.stopReader();
        }
    }

    public abstract interface SwipeHandler{
        public abstract void swipeObtained(Bundle ccBundle);
        public abstract void swipeFailed();
    }
}
