package com.slidepay.coresdk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OrderHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_history, menu);
        return true;
    }


    
}
