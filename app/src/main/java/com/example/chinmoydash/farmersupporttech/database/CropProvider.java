package com.example.chinmoydash.farmersupporttech.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.chinmoydash.farmersupporttech.constant.ConstUtil;


public class CropProvider extends ContentProvider {
    static final int CROP = 100;
    static final int CROP_WITH_NAME = 101;

    static UriMatcher uriMatcher = buildUriMatcher();

    private CropDBHelper dbHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CropContract.AUTHORITY, CropContract.PATH_CROP, CROP);
        matcher.addURI(CropContract.AUTHORITY, CropContract.PATH_CROP_WITH_SYMBOL, CROP_WITH_NAME);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new CropDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {

            case CROP:
                retCursor = db.query(CropContract.CropEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                Log.i("Crop Database", "Reading whole table");
                break;
            case CROP_WITH_NAME:
                retCursor = db.query(CropContract.CropEntry.TABLE_NAME, projection, CropContract.CropEntry.COLUMN_CROP_NAME + " = ?", new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                Log.i("Crop Database", "Reading single crop");
                break;
            default:
                Log.e("Not a valid request", "Check Code");
                break;
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri retUri;
        switch (uriMatcher.match(uri)) {
            case CROP:
                db.insert(
                        CropContract.CropEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                retUri = CropContract.CropEntry.URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        Log.i("Crop Database", "Insertion Successful");
        Cursor cursor = db.query(CropContract.CropEntry.TABLE_NAME, null, null, null, null, null, null);
        Log.i("Row Count", String.valueOf(cursor.getCount()));
        cursor.close();
        Intent intent = new Intent(ConstUtil.CROP_UPDATE_ACTION);
        getContext().sendBroadcast(intent);
        Log.i("Crop Provider", "Broadcast Sent");
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}