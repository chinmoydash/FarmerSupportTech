package com.example.chinmoydash.farmersupporttech.farmer;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chinmoydash.farmersupporttech.ChangePriceFragment;
import com.example.chinmoydash.farmersupporttech.R;
import com.example.chinmoydash.farmersupporttech.database.CropContract;
import com.example.chinmoydash.farmersupporttech.expert.ExpertDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CropDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tb_crop_detail)
    Toolbar toolbar;
    @BindView(R.id.tvCropDesc)
    TextView desc;
    @BindView(R.id.tvCurrPrice)
    TextView currPrice;
    @BindView(R.id.tvLastPrice)
    TextView lastPrice;
    @BindView(R.id.ivCrop)
    ImageView cropImage;
    @BindView(R.id.fabPriceChange)
    FloatingActionButton fabChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getArguments(getIntent());

    }

    private void getArguments(Intent intent) {
        Bundle args = intent.getExtras();
        cropImage.setImageResource(args.getInt(FarmerDetailActivity.ARGUMENT_IMAGE_ID));
        final String crop = args.getString(FarmerDetailActivity.ARGUMENT_NAME);
        getSupportActionBar().setTitle(crop);
        boolean change = args.getBoolean(ExpertDetailActivity.CHANGE);
        if (change) {
            fabChange.setVisibility(View.VISIBLE);
            fabChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangePriceFragment fragment = new ChangePriceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(FarmerDetailActivity.ARGUMENT_NAME, crop);
                    fragment.setArguments(bundle);
                    fragment.show(getSupportFragmentManager(), ChangePriceFragment.class.getName());
                }
            });
        }
        desc.setText(args.getString(FarmerDetailActivity.ARGUMENT_DESCRIPTION));
        setPrice(crop);
    }

    private void setPrice(String crop) {
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(CropContract.CropEntry.URI, crop), null, null, null, null, null);
        try {
            cursor.moveToFirst();
            int currval = cursor.getInt(cursor.getColumnIndex(CropContract.CropEntry.COLUMN_CURR_WEEK_PRICE));
            int lastval = cursor.getInt(cursor.getColumnIndex(CropContract.CropEntry.COLUMN_LAST_WEEK_PRICE));
            currPrice.setText(String.valueOf(currval));
            lastPrice.setText(String.valueOf(lastval));
            cursor.close();
        }
        catch (Exception e) {
            Log.e((getString(R.string.cursor_exception)), e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}
