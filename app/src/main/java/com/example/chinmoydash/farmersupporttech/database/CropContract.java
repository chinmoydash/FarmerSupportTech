package com.example.chinmoydash.farmersupporttech.database;

import android.net.Uri;
import android.provider.BaseColumns;


public class CropContract {

    static final String AUTHORITY = "com.example.chinmoydash";
    static final String PATH_CROP = "crops";
    static final String PATH_CROP_WITH_SYMBOL = "crops/*";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public CropContract() {

    }

    public static class CropEntry implements BaseColumns {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_CROP).build();
        public static final String TABLE_NAME = "crops";
        public static final String COLUMN_CROP_NAME = "crop_name";
        public static final String COLUMN_LAST_WEEK_PRICE = "last_price";
        public static final String COLUMN_CURR_WEEK_PRICE = "curr_price";
    }
}