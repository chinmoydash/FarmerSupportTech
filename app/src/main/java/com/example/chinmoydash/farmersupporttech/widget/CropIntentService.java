package com.example.chinmoydash.farmersupporttech.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.chinmoydash.farmersupporttech.R;


public class CropIntentService extends IntentService {

    public CropIntentService() {
        super(null);
    }

    public CropIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Widget Intent Service", "Handling...");
        int[] appWidgetIds = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, CropWidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {
            Intent remoteIntent = new Intent(this, CropWidgetRemoteService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            remoteIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.initcropwidget);
            views.setRemoteAdapter(R.id.lvWidget, remoteIntent);
            AppWidgetManager.getInstance(this).updateAppWidget(appWidgetId, views);
        }

    }
}