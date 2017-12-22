package com.example.chinmoydash.farmersupporttech;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chinmoydash.farmersupporttech.firebasedata.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddQueryFragment extends DialogFragment {

    View view;
    FragmentActivity mActivity;
    FirebaseDatabase mDatabase;
    DatabaseReference mQuestionsReference;
    @BindView(R.id.metQuestion)
    MaterialEditText metQuestion;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_question, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mDatabase = FirebaseDatabase.getInstance();
        mQuestionsReference = mDatabase.getReference().child("question");


    }

    @OnClick(R.id.tvAddQusetion)
    void addQuestion() {
        String question;
        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        question = metQuestion.getText().toString().trim();

        if (!metQuestion.isCharactersCountValid() || TextUtils.isEmpty(question)) {
            metQuestion.setError(getString(R.string.invalid_question));
            return;
        }

        Question addQuestion = new Question(user, question, new SimpleDateFormat("d-M-y").format(new Date()));
        Log.e((getString(R.string.fb_data)), user + "\n" + question + "\n" + new SimpleDateFormat("d-M-y").format(new Date()));
        mQuestionsReference.push().setValue(addQuestion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dismiss();
                Toast.makeText(mActivity, (R.string.uploadfailurequestion), Toast.LENGTH_LONG).show();
            }
        });


    }


}
