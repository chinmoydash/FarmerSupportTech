package com.example.chinmoydash.farmersupporttech.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.chinmoydash.farmersupporttech.database.CropContract.CropEntry;


public class CropDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "crops.db";
    private final String SQL_QUERY = "CREATE TABLE " + CropEntry.TABLE_NAME + " (" +
            CropEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CropEntry.COLUMN_CROP_NAME + " TEXT, " +
            CropEntry.COLUMN_LAST_WEEK_PRICE + " INTEGER, " +
            CropEntry.COLUMN_CURR_WEEK_PRICE + " INTEGER, " +
            " UNIQUE (" + CropEntry.COLUMN_CROP_NAME + ") ON CONFLICT REPLACE);";

    public CropDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + CropEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
