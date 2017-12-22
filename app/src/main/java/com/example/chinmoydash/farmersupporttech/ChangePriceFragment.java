package com.example.chinmoydash.farmersupporttech;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chinmoydash.farmersupporttech.background.CropRefreshService;
import com.example.chinmoydash.farmersupporttech.database.CropContract;
import com.example.chinmoydash.farmersupporttech.farmer.FarmerDetailActivity;
import com.example.chinmoydash.farmersupporttech.firebasedata.CropData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


public class ChangePriceFragment extends DialogFragment {


    View view;
    MaterialEditText changedPrice;
    FragmentActivity activity;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change, container, false);
        activity = getActivity();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(getString(R.string.cropsKey));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change, null, false);
        dialog.setView(view);
        changedPrice = (MaterialEditText) view.findViewById(R.id.met_new_price);
        dialog.setTitle(getString(R.string.changePrice));
        dialog.setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (changedPrice.isCharactersCountValid()) {
                    Log.e(getString(R.string.price), changedPrice.getText().toString());
                    changePrice(changedPrice.getText().toString().trim());
                }
            }
        }).setNegativeButton(getString(R.string.cancel), null);
        return dialog.create();
    }

    private void changePrice(String price) {
        Log.e(getString(R.string.price), price);
        Bundle args = getArguments();
        String crop = args.getString(FarmerDetailActivity.ARGUMENT_NAME);
        int lastweekval = 0;
        Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(CropContract.CropEntry.URI, crop), null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int lastval = cursor.getInt(cursor.getColumnIndex(CropContract.CropEntry.COLUMN_LAST_WEEK_PRICE));
            lastweekval = lastval;
            cursor.close();
        }
        int currval = Integer.parseInt(price);
        CropData data = new CropData(currval, lastweekval);
        mRef.child(crop).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, (R.string.priceUpdated), Toast.LENGTH_SHORT).show();
                activity.startService(new Intent(activity, CropRefreshService.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
