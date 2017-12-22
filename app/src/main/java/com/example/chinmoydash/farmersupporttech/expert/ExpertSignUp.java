package com.example.chinmoydash.farmersupporttech.expert;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

//import com.example.chinmoydash.farmersupporttech.firebasedata.FirebaseUserProfile;

public class ExpertSignUp extends AppCompatActivity {

    public final static String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";


    MaterialEditText etname;
    MaterialEditText etemail;
    MaterialEditText etmobile;
    MaterialEditText etpassword;
    MaterialEditText etre_password;
    Button signUp;
    ProgressDialog mSignUpDialog;

    //String name, email, mobile, password, re_password;
    String LOG_TAG = "EXPERT SIGNUP";

    FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mUsersReference;
    ChildEventListener mUserListener;
    Context context;
    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_sign_up);
        context = getApplicationContext();
        FirebaseApp.initializeApp(context);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = FirebaseDatabase.getInstance().getReference().child("experts");
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    finish();
            }
        };


        etname = (MaterialEditText) findViewById(R.id.exname);
        etemail = (MaterialEditText) findViewById(R.id.exemail);
        etmobile = (MaterialEditText) findViewById(R.id.exmobile);
        etpassword = (MaterialEditText) findViewById(R.id.expass);
        etre_password = (MaterialEditText) findViewById(R.id.expass1);
        signUp = (Button) findViewById(R.id.btnSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }


    private void submitData() {

        String name = etname.getText().toString().trim();
        String mail = etemail.getText().toString().trim();
        String mobile = etmobile.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        String re_password = etre_password.getText().toString().trim();

        final UserDataModel udm = new UserDataModel(name, mail, mobile);

        if (!etname.isCharactersCountValid())
            return;
        else if (!Patterns.EMAIL_ADDRESS.matcher(etemail.getText().toString()).matches() || !etemail.isCharactersCountValid()) {
            etemail.setError(getString(R.string.invalid_email));
            return;
        }
        if (!etmobile.isCharactersCountValid()) {
            etmobile.setError(getString(R.string.invalid_mobile));
            return;
        }

        if (!etpassword.isCharactersCountValid() || !password.matches(PASSWORD_PATTERN)) {
            etpassword.setError(getString(R.string.invalid_password));
            return;
        }
        if (!etre_password.isCharactersCountValid() || !password.equals(re_password)) {
            etre_password.setError(getString(R.string.invalid_repassword));
            return;
        }

        mSignUpDialog = ProgressDialog.show(this, null, getString(R.string.signing_in), false, false);


        auth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.e(LOG_TAG, getString(R.string.signup_success));
                CropUtils.putUserTypeKey(ExpertSignUp.this, getString(R.string.expert));
                Toast.makeText(ExpertSignUp.this,getString(R.string.signup_success), Toast.LENGTH_LONG).show();
                setUserData(udm, authResult.getUser().getUid());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                Toast.makeText(ExpertSignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mSignUpDialog.dismiss();
                if (task.isSuccessful()) {
                    Log.e(LOG_TAG, getString(R.string.successful));
                    Intent intent = new Intent(getBaseContext(), ExpertDetailActivity.class);
                    startActivity(intent);
                }


            }

        });

    }

    private void setUserData(UserDataModel udm, String uid) {
        mUsersReference.child(uid).setValue(udm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ExpertSignUp.this, getString(R.string.profile_creation_successful), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ExpertSignUp.this,getString(R.string.profile_creation_error), Toast.LENGTH_SHORT).show();
                }
            }

        });

        UserProfileChangeRequest.Builder changeRequest = new UserProfileChangeRequest.Builder();
        changeRequest.setDisplayName(udm.getName());

        auth.getCurrentUser().updateProfile(changeRequest.build()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ExpertSignUp.this,getString(R.string.profile_updated) , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExpertSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

}
