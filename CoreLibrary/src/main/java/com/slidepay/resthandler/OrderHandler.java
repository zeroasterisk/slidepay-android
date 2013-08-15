package com.slidepay.resthandler;

import com.loopj.android.http.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alex on 8/14/13.
 */
public class OrderHandler extends RestHandler{

    SearchFilterArray mParameters;

    public OrderHandler(){
        mParameters = new SearchFilterArray();
        SearchFilter dateFilter = new SearchFilter();

        dateFilter.field = "last_update";
        dateFilter.condition = "great_that";
        //Date now = Da
    }

    public void getSummaries(){

    }

}
