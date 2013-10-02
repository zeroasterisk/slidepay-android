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

    public static final int DEVICE_CONNECTED = 10;
    public static final int DEVICE_DISCONNECTED = 11;
    public static final int DEVICE_IDLE = 12;
    public static final int DEVICE_WAITING = 13;
    public static final int DEVICE_RECORDING = 14;
    public static final int DEVICE_DECODING = 15;

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
                //DECODE_SUCCESS, DECODE_SWIPE_FAIL, DECODE_CRC_ERROR, DECODE_COMM_ERROR, DECODE_UNKNOWN_ERROR;
                String message = "";
                if(decodeResult == ReaderController.DecodeResult.DECODE_SWIPE_FAIL){
                    message = "swipe failed";
                }
                if(decodeResult == ReaderController.DecodeResult.DECODE_CRC_ERROR){
                    message = "CRC error";
                }
                if(decodeResult == ReaderController.DecodeResult.DECODE_COMM_ERROR){
                    message = "Communication error";
                }
                if(decodeResult == ReaderController.DecodeResult.DECODE_UNKNOWN_ERROR){
                    message = "unknown error";
                }
                Log.w(TAG,"Decode Error: "+message);
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
                mUserHandler.stateChanged(DEVICE_DECODING);
            }

            @Override
            public void onWaitingForCardSwipe() {
                Log.w(TAG,"onWaitingForCardSwipe");
            }

            @Override
            public void onWaitingForDevice() {
                Log.w(TAG,"onWaitingForDevice");
                mUserHandler.stateChanged(DEVICE_WAITING);
            }

            @Override
            public void onDevicePlugged() {
                Log.w(TAG,"onDevicePlugged");
                mUserHandler.stateChanged(DEVICE_CONNECTED);
            }

            @Override
            public void onDeviceUnplugged() {
                Log.w(TAG,"onDeviceUnplugged");
                mUserHandler.stateChanged(DEVICE_DISCONNECTED);

            }
        });
        mReaderController.setDetectDeviceChange(true);
        mReaderController.setMaxVolume(1000);
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
    public void stopListening(){
        try{
            mReaderController.stopReader();
        }catch(IllegalStateException e){};//we just don't care.
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
        public abstract void stateChanged( int newState);
    }
}