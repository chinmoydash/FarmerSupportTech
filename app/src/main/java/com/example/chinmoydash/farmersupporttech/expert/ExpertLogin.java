package com.example.chinmoydash.farmersupporttech.expert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chinmoydash.farmersupporttech.R;
import com.example.chinmoydash.farmersupporttech.background.CropUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ExpertLogin extends AppCompatActivity {

    final String LOG_TAG = "SIGNIN FRAGMENT";

    Button signin;
    MaterialEditText etemail;
    MaterialEditText etpassword;

    FirebaseAuth mFirebaseAuth;
    String email, pass;
    ProgressDialog mSignInDialog;
    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_login);

        mFirebaseAuth = FirebaseAuth.getInstance();


        signin = (Button) findViewById(R.id.btnSignIn);
        etemail = (MaterialEditText) findViewById(R.id.etemail);
        etpassword = (MaterialEditText) findViewById(R.id.etpass);
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    finish();
            }
        };


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    private void signIn() {

        email = etemail.getText().toString().trim();
        pass = etpassword.getText().toString().trim();

        if (!etemail.isCharactersCountValid() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError(getString(R.string.invalid_email));
            return;
        }
        if (!etpassword.isCharactersCountValid() || !pass.matches(ExpertSignUp.PASSWORD_PATTERN)) {
            etpassword.setError(getString(R.string.invalid_password));
            return;
        }
        mSignInDialog = ProgressDialog.show(this, null,(getString(R.string.signing_in)), false, false);

        mFirebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(ExpertLogin.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(ExpertLogin.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            mSignInDialog.dismiss();
                        } else {
                            CropUtils.putUserTypeKey(ExpertLogin.this, getString(R.string.expert));
                            Intent intent = new Intent(ExpertLogin.this, ExpertDetailActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mListener);
    }


}