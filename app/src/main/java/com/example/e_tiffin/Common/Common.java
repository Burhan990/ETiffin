package com.example.e_tiffin.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.e_tiffin.Model.User;
import com.example.e_tiffin.SignIn;


public class Common {

    public static User currentUser;

    public static final String DELETE="Delete";
    public static final String USER_KEY="User";
    public static final String PWD_KEY="Password";



    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);



        if (connectivityManager != null){
            //NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();

            if (info != null){

                for (int i=0;i<info.length;i++){
                    if (info[i].getState()==NetworkInfo.State.CONNECTED);
                    return true;
                }
            }
        }
        return false;
    }





   /* protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }
    */


    public static boolean CheckInternet(Context context) {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }

}
