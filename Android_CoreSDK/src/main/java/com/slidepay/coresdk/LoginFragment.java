package com.slidepay.coresdk;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.slidepay.models.CurrentSession;
import com.slidepay.resthandler.LoginHandler;
import com.slidepay.resthandler.OrderHandler;
import com.slidepay.resthandler.ResponseHandler;

import org.json.JSONObject;

/**
 * Created by Alex on 8/22/13.
 */
public class LoginFragment extends Fragment{

    private LoginHandler mLoginHandler;
    private OrderHandler mOrderHandler;
    private Button mSubmit;
    private EditText mEmailField;
    private EditText mPasswordField;
    private static final String TAG = "SP_LoginFragment";
    private ProgressDialog mDialog;


    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.w(TAG,"onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        Log.w(TAG,"inflate the view for the login fragment");
        View v = inflater.inflate(R.layout.fragment_login,parent,false);
//        View v = inflater.inflate(R.layout
//        View v = inflater.inflate(R.layout.fragment_login, parent, false); //we will be adding this view to the parent view, but not through this function.

        //handles to all our views
        mEmailField = (EditText)v.findViewById(R.id.login_email);
        mPasswordField = (EditText)v.findViewById(R.id.login_password);
        mSubmit = (Button)v.findViewById(R.id.login_submit);

        //add a click listener to the submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new ProgressDialog(getActivity());
                mDialog.setMessage("Logging you in. Please Wait");
                mDialog.setCancelable(false);
                mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mDialog.show();
                login();
            }
        });

        //set debug values for email and password
        mEmailField.setText(R.string.login_debug_email);
        mPasswordField.setText(R.string.login_debug_password);
        //

        return v;
    }

    public void login(){
        String email = mEmailField.getText() != null ? mEmailField.getText().toString() : "";
        String password = mPasswordField.getText() != null ? mPasswordField.getText().toString() : "";
        Log.d(TAG,"logging in w/ email: "+email+"   and pword: "+password);
        mLoginHandler = new LoginHandler(email,password);
        mLoginHandler.login(new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                JSONObject jsonResponse = (JSONObject)response;
                Log.d(TAG, "login success. Response: " + jsonResponse);
                getTokenDetails();
            }

            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                //popups
                Log.d(TAG,"login failed. Response: "+response);
                mDialog.dismiss();
            }
        });
    }

    public void getTokenDetails(){
        mLoginHandler.getUserDetails(new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG,"token detail success!");
                Log.d(TAG,"current session: "+ CurrentSession.getInstance().toString());
                //grab some orders
                loginSuccess();
            }
            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                Log.d(TAG,"token detail failure!");
                loginFailed("Login failed.");
            }
        });
    }

    public void loginFailed(String response){
        mDialog.dismiss();

    }
    public void loginSuccess(){
        mDialog.setProgress(mDialog.getMax());
    }

}
