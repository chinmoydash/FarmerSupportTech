package com.example.chinmoydash.farmersupporttech.background;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.example.chinmoydash.farmersupporttech.R;
import com.example.chinmoydash.farmersupporttech.database.CropContract;
import com.example.chinmoydash.farmersupporttech.firebasedata.CropData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CropRefreshService extends IntentService {

    FirebaseDatabase database;
    DatabaseReference reference;

    public CropRefreshService(String name) {
        super(name);
    }

    public CropRefreshService() {
        super(null);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("CropRefreshService", "Handling Intent Service");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(getString(R.string.cropsKey));
        String[] crops = getResources().getStringArray(R.array.names);
        ArrayList<ContentValues> values = new ArrayList<>();
        for (final String crop : crops) {
            Log.i("Crop", crop);
            reference.child(crop).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CropData data = dataSnapshot.getValue(CropData.class);
                    Log.i("Value", String.valueOf(data.getCurrval()));
                    Log.i("Value", dataSnapshot.getKey());
                    ContentValues cropValues = new ContentValues();
                    cropValues.put(CropContract.CropEntry.COLUMN_CROP_NAME, crop);
                    cropValues.put(CropContract.CropEntry.COLUMN_CURR_WEEK_PRICE, data.getCurrval());
                    cropValues.put(CropContract.CropEntry.COLUMN_LAST_WEEK_PRICE, data.getLastval());
                    getContentResolver().insert(CropContract.CropEntry.URI, cropValues);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("DB Error", databaseError.getMessage());
                }
            });
        }
        //database.goOffline();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Intent Service", "Created");

    }
}
