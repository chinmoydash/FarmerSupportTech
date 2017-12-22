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

import com.example.chinmoydash.farmersupporttech.firebasedata.News;
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


public class AddNewsFragment extends DialogFragment {


    View view;
    FragmentActivity mActivity;
    FirebaseDatabase mDatabase;
    DatabaseReference mNewsReference;
    @BindView(R.id.metNews)
    MaterialEditText metNews;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_news, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mDatabase = FirebaseDatabase.getInstance();
        mNewsReference = mDatabase.getReference().child("news");
    }

    @OnClick(R.id.tvAddNews)
    void addNews() {
        String news;
        String expert  = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        news = metNews.getText().toString().trim();
        if (!metNews.isCharactersCountValid() || TextUtils.isEmpty(news)) {
            metNews.setError(getString(R.string.invalid_question));
            return;
        }

        News addNews = new News(expert, news, new SimpleDateFormat("d-M-y").format(new Date()));
        Log.e((getString(R.string.fb_data)), expert + "\n" + news + "\n" + new SimpleDateFormat("d-M-y").format(new Date()));
        mNewsReference.push().setValue(addNews).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dismiss();
                Toast.makeText(mActivity,(R.string.uploadfailurenews) , Toast.LENGTH_LONG).show();
            }
        });


    }
}