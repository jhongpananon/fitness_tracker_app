package com.jhongpananon.sqlite_project;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.support.design.widget.TabLayout;

import com.google.android.gms.maps.model.CameraPosition;


public class MapsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
        , MapFragment.LocationChangeListener {

    private CameraPosition mCameraPosition;


    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        ViewPager viewPager = findViewById(R.id.viewpager);

        adapter = new CategoryAdapter(this, getSupportFragmentManager());
        adapter.addFrag(new MapFragment());
        adapter.addFrag(new LocationListFragment());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onLocationChange(Bundle bundle) {
        Log.i("callback", "hi");
        if (adapter != null) {
            adapter.onLocationChange(bundle);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i("page selected", "onPageSelected Called");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}