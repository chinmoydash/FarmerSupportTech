package com.example.chinmoydash.farmersupporttech.background;

import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


public class CropJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, CropRefreshService.class));
        jobFinished(job, false);
        Log.i("CropJobService", "Started");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i("CropJobService", "Stopped");
        return true;
    }
}