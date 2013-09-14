package com.slidepay.models;

import android.nfc.Tag;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alex on 8/14/13.
 */
public class CurrentSession {
    private static CurrentSession ourInstance = new CurrentSession();
    private static final String TAG = "SP_CurrentSession";

    protected String first_name;
    protected String last_name;
    protected String location_name;
    protected String company_name;
    protected String time_zone;
    protected String ip_address;
    protected String created;
    protected String email;
    protected int is_clerk;
    protected int is_comgr;
    protected int is_locmgr;
    protected int is_admin;
    protected int    user_master_id;
    protected int    company_id;
    protected int    location_id;

    public static CurrentSession getInstance() {
        return ourInstance;
    }

    private CurrentSession() {
    }

    public boolean setSessionFromJSON(JSONObject JSONsession){
        Gson gson = new Gson();
        Log.d(TAG,"trying to copy JSON into current session. Supplied JSON: "+JSONsession.toString());
        CurrentSession session = gson.fromJson(JSONsession.toString(), CurrentSession.class);

        if(session == null){
            Log.e(TAG,"Gson was unable to convert passed in JSON into a CurrentSession object.");
            Log.e(TAG,"JSON: "+JSONsession.toString());
            return false;
        }

        this.first_name = session.first_name;
        this.last_name = session.last_name;
        this.location_name = session.location_name;
        this.company_name = session.company_name;
        this.time_zone = session.time_zone;
        this.ip_address = session.ip_address;
        this.created = session.created;
        this.email = session.email;
        this.is_clerk = session.is_clerk;
        this.is_comgr = session.is_comgr;
        this.is_locmgr = session.is_locmgr;
        this.is_admin = session.is_admin;
        this.user_master_id = session.user_master_id;
        this.company_id = session.company_id;
        this.location_id = session.location_id;

        return true;
    }

    @Override public String toString(){
        String asString = "first_name = "+this.first_name + "\n";
        asString = asString+"last_name = "+this.last_name + "\n";
        asString = asString+"location_name = "+this.location_name + "\n";
        asString = asString+"company_name = "+this.company_name + "\n";
        asString = asString+"user_master_id = "+this.user_master_id + "\n";
        asString = asString+"company_id = "+this.company_id + "\n";
        asString = asString+"location_id = "+this.location_id + "\n";
        return asString;
    }
}
