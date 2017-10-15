package com.shreyasbhandare.ruevents.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkStatus {
    public static String checkNetworkStatus(Context context) {

        String networkStatus ="";
        final ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Check Wifi
        final android.net.NetworkInfo wifi = manager.getActiveNetworkInfo();
        //Check for mobile data
        final android.net.NetworkInfo mobile = manager.getActiveNetworkInfo();

        if(wifi==null&&mobile==null)
            return "noNetwork";
        else if(wifi==null && mobile.getType() == ConnectivityManager.TYPE_MOBILE)
            return "mobileData";
        else if(wifi.getType() == ConnectivityManager.TYPE_WIFI && mobile==null)
            return "wifi";
        else
            return "both Wifi and Mobile";

    }
}
