package com.example.chinmoydash.farmersupporttech.farmer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chinmoydash.farmersupporttech.R;
import com.example.chinmoydash.farmersupporttech.background.CropUtils;
import com.example.chinmoydash.farmersupporttech.firebasedata.UserDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class FarmerSignUp extends AppCompatActivity {

    public final static String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    String LOG_TAG = "FARMER  SIGNUP";
    MaterialEditText et_name;
    MaterialEditText et_email;
    MaterialEditText et_mobile;
    MaterialEditText et_password;
    MaterialEditText et_re_password;
    Button signUp;
    Context context;
    ProgressDialog mSignUpDialog;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mListener;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mUsersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_sign_up);
        context = getApplicationContext();
        FirebaseApp.initializeApp(context);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = FirebaseDatabase.getInstance().getReference().child("farmer");

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    finish();
            }
        };


        et_name = (MaterialEditText) findViewById(R.id.etname);
        et_email = (MaterialEditText) findViewById(R.id.etemail);
        et_mobile = (MaterialEditText) findViewById(R.id.etmobile);
        et_password = (MaterialEditText) findViewById(R.id.etpass);
        et_re_password = (MaterialEditText) findViewById(R.id.etpass1);
        signUp = (Button) findViewById(R.id.btnSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();


            }
        });
    }

    private void submitData() {

        String name = et_name.getText().toString().trim();
        String mail = et_email.getText().toString().trim();
        String mobile = et_mobile.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String re_password = et_re_password.getText().toString().trim();

        final UserDataModel udm = new UserDataModel(name, mail, mobile);


        if (!et_name.isCharactersCountValid())
            return;
        else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches() || !et_email.isCharactersCountValid()) {
            et_email.setError(getString(R.string.invalid_email));
            return;
        }
        if (!et_mobile.isCharactersCountValid()) {
            et_mobile.setError(getString(R.string.invalid_mobile));
            return;
        }

        if (!et_password.isCharactersCountValid() || !password.matches(PASSWORD_PATTERN)) {
            et_password.setError(getString(R.string.invalid_password));
            return;
        }
        if (!et_re_password.isCharactersCountValid() || !password.equals(re_password)) {
            et_re_password.setError(getString(R.string.invalid_repassword));
            return;
        }

        mSignUpDialog = ProgressDialog.show(this, null,getString(R.string.signing_in), false, false);


        auth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.e(LOG_TAG, getString(R.string.signup_success));
                mSignUpDialog.dismiss();
                Toast.makeText(FarmerSignUp.this,getString(R.string.signup_success), Toast.LENGTH_LONG).show();
                CropUtils.putUserTypeKey(FarmerSignUp.this, getString(R.string.farmer));
                setUserData(udm, authResult.getUser().getUid());


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                Toast.makeText(FarmerSignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mSignUpDialog.dismiss();
                if (task.isSuccessful()) {
                    Log.e(LOG_TAG, getString(R.string.successful));
                    Intent intent = new Intent(getBaseContext(), FarmerDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(mListener);
    }

    private void setUserData(UserDataModel udm, String uid) {
        mUsersReference.child(uid).setValue(udm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(FarmerSignUp.this, getString(R.string.profile_creation_successful), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(FarmerSignUp.this, getString(R.string.profile_creation_error), Toast.LENGTH_SHORT).show();
                }
            }

        });

        UserProfileChangeRequest.Builder changeRequest = new UserProfileChangeRequest.Builder();
        changeRequest.setDisplayName(udm.getName());

        auth.getCurrentUser().updateProfile(changeRequest.build()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FarmerSignUp.this,getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FarmerSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}