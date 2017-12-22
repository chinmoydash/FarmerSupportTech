package com.example.chinmoydash.farmersupporttech.expert;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.chinmoydash.farmersupporttech.CropsListFragment;
import com.example.chinmoydash.farmersupporttech.NewsFragment;
import com.example.chinmoydash.farmersupporttech.QueryFragment;
import com.example.chinmoydash.farmersupporttech.R;
import com.example.chinmoydash.farmersupporttech.background.CropUtils;
import com.example.chinmoydash.farmersupporttech.farmer.CropDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.chinmoydash.farmersupporttech.farmer.FarmerDetailActivity.ARGUMENT_DESCRIPTION;
import static com.example.chinmoydash.farmersupporttech.farmer.FarmerDetailActivity.ARGUMENT_IMAGE_ID;
import static com.example.chinmoydash.farmersupporttech.farmer.FarmerDetailActivity.ARGUMENT_NAME;

public class ExpertDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CropsListFragment.OnCropSelected {

    public static final String CROP_TAG = "Crops", NEWS_TAG = "News", QUERY_TAG = "Query", CHANGE = "change";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private NavigationView navigationView;
    private String selectedItem, selectedItemKey = "ITEM_SELECTED";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(selectedItemKey, selectedItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    finish();
                    startActivity(new Intent(ExpertDetailActivity.this, ExpertActivity.class));
                } else {
                    View view = navigationView.getHeaderView(0);
                    TextView email = (TextView) view.findViewById(R.id.tvNavEmailExp);
                    TextView name = (TextView) view.findViewById(R.id.tvNavnameExp);
                    email.setText(user.getEmail());
                    name.setText(user.getDisplayName());
                }

            }
        };
        if (savedInstanceState == null)
            replaceFragment(new CropsListFragment(), CROP_TAG);
        else {
            selectedItem = savedInstanceState.getString(selectedItemKey);
            if (selectedItem == null)
                return;
            Fragment fragment;
            switch (selectedItem) {
                case CROP_TAG:
                    fragment = new CropsListFragment();
                    navigationView.setCheckedItem(R.id.allCrops);
                    break;
                case NEWS_TAG:
                    fragment = new NewsFragment();
                    navigationView.setCheckedItem(R.id.addNews);
                    break;
                case QUERY_TAG:
                    fragment = new QueryFragment();
                    navigationView.setCheckedItem(R.id.viewQuery);
                    break;
                default:
                    fragment = new CropsListFragment();
                    navigationView.setCheckedItem(R.id.allCrops);
                    break;
            }
            replaceFragment(fragment, selectedItem);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().findFragmentByTag(CROP_TAG) == null) {
            navigationView.setCheckedItem(R.id.allCrops);
            replaceFragment(new CropsListFragment(), CROP_TAG);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.expert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out_exp) {
            mAuth.signOut();
            CropUtils.putUserTypeKey(this, getString(R.string.nouser));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

       if (id == R.id.allCrops) {
            // Show All the Crops
            replaceFragment(new CropsListFragment(), CROP_TAG);
        }
       if (id == R.id.viewQuery) {

            replaceFragment(new QueryFragment(), QUERY_TAG);

        } else if (id == R.id.addNews) {

            replaceFragment(new NewsFragment(), NEWS_TAG);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void replaceFragment(Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.exp_container, fragment, tag).commit();
        getSupportActionBar().setTitle(tag);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onCropSelected(int imageResId, String name, String description) {
        final Bundle args = new Bundle();
        args.putInt(ARGUMENT_IMAGE_ID, imageResId);
        args.putString(ARGUMENT_NAME, name);
        args.putString(ARGUMENT_DESCRIPTION, description);
        args.putBoolean(CHANGE, true);
        Intent intent = new Intent(this, CropDetailsActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }
}
