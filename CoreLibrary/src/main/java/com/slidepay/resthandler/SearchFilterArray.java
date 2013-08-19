package com.slidepay.resthandler;

import java.util.LinkedList;

/**
 * Created by Alex on 8/14/13.
 */
public class SearchFilterArray {
//    public static final boolean raw_data = true;
    private boolean raw_data;
    public LinkedList<SearchFilter> sfa;

    public SearchFilterArray(){
        raw_data = true;
    }

    public void addFilter(SearchFilter filter){
        if(sfa == null){
            sfa = new LinkedList<SearchFilter>();
        }
        sfa.add(filter);
    }

    public void removeFilters(){
        if(sfa != null){
            sfa.remove();
        }
    }
}

class SearchFilter{
    public String condition;
    public String value;
    public String field;
    public SearchFilter(){
    }
}
