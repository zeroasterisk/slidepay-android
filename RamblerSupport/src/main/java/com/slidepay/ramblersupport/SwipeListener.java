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
    private String mLastGeneralError;

    public static final int ERROR_GENERAL = 0;
    public static final int ERROR_DECODE = 1;
    public static final int ERROR_NO_DEVICE = 2;
    public static final int ERROR_TIMEOUT = 3;

    public SwipeListener(Context context, SwipeHandler handler){
        mLastGeneralError = null;
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
                mUserHandler.swipeFailed( ERROR_DECODE );
            }

            @Override
            public void onError(String s) {
                Log.w(TAG,"onError");
                mLastGeneralError = s;
                mUserHandler.swipeFailed( ERROR_GENERAL );
            }

            @Override
            public void onInterrupted() {
                Log.w(TAG,"onInterrupted");
                try {
                    if(SwipeListener.this.isListening)
                        SwipeListener.this.setListening( false );
                } catch( IllegalStateException ignore ) {}
                try {
                    SwipeListener.this.setListening( true );
                } catch( IllegalStateException ignore ) {}

            }

            @Override
            public void onNoDeviceDetected() {
                Log.w(TAG,"onNoDeviceDetected");
                mUserHandler.swipeFailed( ERROR_NO_DEVICE );
            }

            @Override
            public void onTimeout() {
                Log.w(TAG,"onTimeout");
                mUserHandler.swipeFailed( ERROR_TIMEOUT );
                if(SwipeListener.this.mReaderController != null){
                    SwipeListener.this.setListening(true);
                }
            }

            @Override
            public void onDecodingStart() {
                Log.w(TAG,"onDecodingStart");
            }

            @Override
            public void onWaitingForCardSwipe() {
                Log.w(TAG,"onWaitingForCardSwipe");
            }

            @Override
            public void onWaitingForDevice() {
                Log.w(TAG,"onWaitingForDevice");
            }

            @Override
            public void onDevicePlugged() {
                Log.w(TAG,"onDevicePlugged");
            }

            @Override
            public void onDeviceUnplugged() {
                Log.w(TAG,"onDeviceUnplugged");
            }
        });
    }
    private SwipeListener(){}

    public boolean isListening() {
        return isListening;
    }

    /**
     * Flag reader device presence/attached to android device
     * @return true if
     * @throws IllegalStateException when reader is already released
     */
    public boolean isDevicePresent() throws IllegalStateException {
        if ( mReaderController == null )
            throw new IllegalStateException( "Already released reader" );
        return mReaderController.isDevicePresent();
    }

    /**
     * Sets the listening state for the reader
     * @param listening state
     * @throws IllegalStateException when reader can not reach new state from current
     */
    public void setListening(boolean listening) throws IllegalStateException {
        if ( mReaderController == null )
            throw new IllegalStateException( "Already released reader" );
        if(SwipeListener.this.mReaderController.getReaderState() == ReaderController.ReaderControllerState.STATE_IDLE){
            isListening = listening;
            if(isListening){
                mReaderController.startReader();
            }else{
                mReaderController.stopReader();
            }
        }
    }

    /**
     * Releases the resource of this listener
     */
    public void release() {
        mReaderController.deleteReader();
        mReaderController = null;
    }
    /**
     * Returns the last general error
     * @return the error
     */
    public String getLastGeneralError() {
        return mLastGeneralError;
    }

    public abstract interface SwipeHandler{
        public abstract void swipeObtained(Bundle ccBundle);
        public abstract void swipeFailed( int errorCode );
    }
}