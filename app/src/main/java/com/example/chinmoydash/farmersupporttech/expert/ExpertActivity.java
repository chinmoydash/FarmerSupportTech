package com.example.chinmoydash.farmersupporttech.expert;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.chinmoydash.farmersupporttech.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ExpertActivity extends AppCompatActivity {


    Button login, signup;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        mFirebaseAuth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.btnLogin);
        signup = (Button) findViewById(R.id.btnSignUp);


        login.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ExpertActivity.this, ExpertLogin.class);
                        startActivity(intent);
                    }

                });
        signup.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ExpertActivity.this, ExpertSignUp.class);
                        startActivity(intent);
                    }

                });
        mStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    finish();
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mStateListener);
    }
}
