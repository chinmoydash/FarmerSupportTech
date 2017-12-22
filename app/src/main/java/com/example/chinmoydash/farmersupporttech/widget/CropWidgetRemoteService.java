package com.example.chinmoydash.farmersupporttech.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.chinmoydash.farmersupporttech.R;
import com.example.chinmoydash.farmersupporttech.database.CropContract;

import java.text.DecimalFormat;


public class CropWidgetRemoteService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CropRemoteFactory(getApplicationContext(), intent);
    }

    class CropRemoteFactory implements RemoteViewsFactory {
        private Context mContext;
        private DecimalFormat ruppeeFormat;
        private Cursor mCursor;
        private int[] images;
        private int appWidgetId;

        CropRemoteFactory(Context context, Intent intent) {
            mContext = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        }

        @Override
        public void onCreate() {
            mCursor = mContext.getContentResolver().query(CropContract.CropEntry.URI, null, null, null, null);
            Log.i("Cursor Count", String.valueOf(mCursor.getCount()));
            Resources resources = mContext.getResources();
            String[] mCropNames = resources.getStringArray(R.array.names);
            final TypedArray typedArray = resources.obtainTypedArray(R.array.images);
            final int imageCount = mCropNames.length;
            images = new int[imageCount];
            for (int i = 0; i < imageCount; i++) {
                images[i] = typedArray.getResourceId(i, 0);
            }
            typedArray.recycle();
        }

        @Override
        public void onDataSetChanged() {
            mCursor = mContext.getContentResolver().query(CropContract.CropEntry.URI, null, null, null, null);
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            mCursor.moveToPosition(i);
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_crop_widget);
            views.setTextViewText(R.id.tvCropName, mCursor.getString(mCursor.getColumnIndex(CropContract.CropEntry.COLUMN_CROP_NAME)));
            views.setTextViewText(R.id.tvCropPrice, mCursor.getString(mCursor.getColumnIndex(CropContract.CropEntry.COLUMN_CURR_WEEK_PRICE)));
            views.setImageViewResource(R.id.ivWidget, images[i]);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return mCursor.getCount();
        }

        @Override
        public long getItemId(int i) {
            return appWidgetId;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}