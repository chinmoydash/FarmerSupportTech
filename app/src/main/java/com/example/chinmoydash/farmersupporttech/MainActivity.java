package com.example.chinmoydash.farmersupporttech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.chinmoydash.farmersupporttech.expert.ExpertActivity;
import com.example.chinmoydash.farmersupporttech.expert.ExpertDetailActivity;
import com.example.chinmoydash.farmersupporttech.farmer.FarmerActivity;
import com.example.chinmoydash.farmersupporttech.farmer.FarmerDetailActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button farmer, expert;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        farmer = (Button) findViewById(R.id.butFarmer);
        expert = (Button) findViewById(R.id.butExpert);

        farmer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, FarmerActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });

        expert.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ExpertActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    String userType = preferences.getString(getString(R.string.userTypeKey), null);
                    if (userType != null && userType.equals(getString(R.string.farmer)))
                        startActivity(new Intent(MainActivity.this, FarmerDetailActivity.class));
                    else if (userType != null && userType.equals(getString(R.string.expert)))
                        startActivity(new Intent(MainActivity.this, ExpertDetailActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mListener);
    }
}