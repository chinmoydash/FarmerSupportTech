package com.example.chinmoydash.farmersupporttech.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.chinmoydash.farmersupporttech.R;
import com.example.chinmoydash.farmersupporttech.constant.ConstUtil;



public class CropWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(String.valueOf((R.string.widget_update)), String.valueOf((R.string.called)));
        Intent intent = new Intent(context, CropIntentService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(intent);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(String.valueOf((R.string.on_receive)),String.valueOf((R.string.called)));
        if (intent.getAction().equals(ConstUtil.CROP_UPDATE_ACTION)) {
            Log.i(String.valueOf((R.string.crop_widget_provider)), String.valueOf((R.string.broadcast_received)));
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, CropWidgetProvider.class)));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds((new ComponentName(context, CropWidgetProvider.class))), R.id.lvWidget);
        }
    }
}