package com.example.chinmoydash.farmersupporttech;


import com.firebase.client.Firebase;
import com.firebase.client.Logger;
import com.google.firebase.FirebaseApp;


public class FarmerSupportTechApplication extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
    }


}
