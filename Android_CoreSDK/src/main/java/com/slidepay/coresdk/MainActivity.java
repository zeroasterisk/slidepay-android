package com.slidepay.coresdk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.slidepay.resthandler.*;

public class MainActivity extends Activity {

    LoginHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new LoginHandler("brent@getcube.com","p@ssw0rd");
        mHandler.login();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
