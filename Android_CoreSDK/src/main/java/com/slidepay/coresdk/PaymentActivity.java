package com.slidepay.coresdk;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;



/**
 * Created by Alex on 9/12/13.
 */
public class PaymentActivity extends Activity{

    private static final String TAG = "SP_PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.w(TAG, "On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        FragmentManager manager = getFragmentManager();
        Fragment loginFragment = manager.findFragmentById(R.id.paymentContainer);
        if(loginFragment == null){
            loginFragment = new PaymentFragment();
            //Fragments are uniquely identified using the parent container view ID.
            //In this case, that's the FrameLayout defined in activity_main.xml
            manager.beginTransaction().add(R.id.paymentContainer, loginFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(android.R.menu.main, menu);
        return true;
    }
}
