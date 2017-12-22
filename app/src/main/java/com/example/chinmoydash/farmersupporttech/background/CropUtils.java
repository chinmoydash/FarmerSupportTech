package com.example.chinmoydash.farmersupporttech.background;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.chinmoydash.farmersupporttech.R;


public class CropUtils {

    CropUtils() {
    }

    public static boolean networkUp(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean initialized(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.initializedKey), false);
    }

    public static void putUserTypeKey(Context context, String user) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.userTypeKey), user);
        editor.apply();

    }
}