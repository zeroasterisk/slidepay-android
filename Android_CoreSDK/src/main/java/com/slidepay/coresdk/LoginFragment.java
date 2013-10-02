package com.slidepay.coresdk;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    private static final boolean overrideLogin = true;
    private ProgressDialog mDialog;


    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.w(TAG,"onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        Log.w(TAG,"inflate the view for the login fragment");
        View v = inflater.inflate(R.layout.fragment_login,parent,false);

        //handles to all our views
        mEmailField = (EditText)v.findViewById(R.id.login_email);
        mPasswordField = (EditText)v.findViewById(R.id.login_password);
        mSubmit = (Button)v.findViewById(R.id.login_submit);

        //add a click listener to the submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(overrideLogin){
                    loginSuccess();
                }else{
                    if(checkPermissions()){
                        login();
                    }
                }
            }
        });

        return v;
    }

    public boolean checkPermissions(){

        String title = "Please check your permissions";
        String message = null;

        if(getActivity().getApplicationContext().checkCallingOrSelfPermission("android.permission.INTERNET") != PackageManager.PERMISSION_GRANTED){
            message = "This application requires internet permissions in order to run.";
        }
        if(getActivity().getApplicationContext().checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != PackageManager.PERMISSION_GRANTED){
            message = "This application requires network state permissions in order to run.";
        }if(getActivity().getApplicationContext().checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED){
            message = "This application requires audio recording permissions in order to run.";
        }

        if(message != null){
            showAlertDialog(title,message);
            return false;
        }else{
            return true;
        }
    }

    public void showAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(title)
                .setTitle(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showUsernamePasswordAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please enter both a username and password.")
                    .setTitle("Unable to Begin Login Process");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showProgressDialog(){
        if(mDialog != null){
            mDialog.dismiss();
        }
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Logging you in. Please Wait");
        mDialog.setCancelable(false);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();
    }

    public void login(){
        //check permissions
        String email = mEmailField.getText() != null ? mEmailField.getText().toString() : "";
        String password = mPasswordField.getText() != null ? mPasswordField.getText().toString() : "";
        Log.d(TAG,"logging in w/ email: "+email+"   and pword: "+password);
        mSubmit.setEnabled(false);
        showProgressDialog();
        mLoginHandler = new LoginHandler(email,password);
        boolean didLogin = mLoginHandler.login(new ResponseHandler() {
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
                loginFailed("login failed");
            }
        });
        if(!didLogin){
            mDialog.dismiss();
            showUsernamePasswordAlert();
            loginFailed("");
        }

    }

    public void getTokenDetails(){
        mLoginHandler.getUserDetails(new ResponseHandler() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG, "token detail success!");
                Log.d(TAG, "current session: " + CurrentSession.getInstance().toString());
                //grab some orders
                loginSuccess();
            }

            @Override
            public void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription) {
                Log.d(TAG, "token detail failure!");
                loginFailed("Login failed.");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSubmit != null){
            mSubmit.setEnabled(true);
        }else{
            Log.w(TAG,"onResume() - mSubmit is null");
        }
    }

    public void loginFailed(String response){
        mSubmit.setEnabled(true);
        if(mDialog.isShowing()){
            mDialog.dismiss();
        }
    }
    public void loginSuccess(){ //disable the login button and transition to the next screen
        mSubmit.setEnabled(false);
        if(mDialog != null && mDialog.isShowing()){
            mDialog.setProgress(mDialog.getMax());
            mDialog.dismiss();
        }
        Log.d(TAG,"loginSuccess - transitioning");
        Intent intent = new Intent(getActivity(),PaymentActivity.class);
        startActivity(intent);
    }

}
