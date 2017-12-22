package com.example.chinmoydash.farmersupporttech.background;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.chinmoydash.farmersupporttech.R;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;

import static com.example.chinmoydash.farmersupporttech.database.CropContract.CropEntry;


public class CropSyncJobs {

    CropSyncJobs() {
    }

    public static void syncCropsImmediately(Context context) {
        context.startService(new Intent(context, CropRefreshService.class));
    }

    public static void syncPerodically(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = dispatcher.newJobBuilder()
                .setService(CropJobService.class)
                .setRecurring(true)
                .setTag("crops")
                .setReplaceCurrent(true)
                .setLifetime(Lifetime.FOREVER)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTrigger(Trigger.executionWindow(0, 30))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).build();
        try {
            dispatcher.schedule(job);
        } catch (Exception e) {
            Log.e("Dispatcher Error", e.getMessage());
        }
        Log.i("Dispatcher", "Started");
    }

    private static void writeDefaultData(Context context) {
        /**
         * TODO write crops prices with 0  0 value
         */
        String[] crops = context.getResources().getStringArray(R.array.names);
        ArrayList<ContentValues> values = new ArrayList<>();
        for (String crop : crops) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CropEntry.COLUMN_CROP_NAME, crop);
            contentValues.put(CropEntry.COLUMN_CURR_WEEK_PRICE, 0);
            contentValues.put(CropEntry.COLUMN_LAST_WEEK_PRICE, 0);
            values.add(contentValues);
        }

        context.getContentResolver().bulkInsert(CropEntry.URI, values.toArray(new ContentValues[values.size()]));
    }

    public static void initialize(Context context) {
        if (!CropUtils.initialized(context)) {
            writeDefaultData(context);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean(context.getString(R.string.initializedKey), true);
            editor.apply();
        }
        syncPerodically(context);
        syncCropsImmediately(context);
    }
}
