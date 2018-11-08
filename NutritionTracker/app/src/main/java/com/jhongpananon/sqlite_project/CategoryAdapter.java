package com.jhongpananon.sqlite_project;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends FragmentPagerAdapter implements MapFragment.LocationChangeListener{


    /** Context of the app */
    private Context mContext;
    private List<Fragment> mFrags= new ArrayList<>();


    public CategoryAdapter(Context context, FragmentManager fm) {

        super(fm);
        mContext = context;
    }

    public void addFrag(Fragment frag) {
        mFrags.add(frag);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        if (position == 0) {
            frag = mFrags.get(position);
        } else if (position == 1) {
            frag = mFrags.get(position);
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence seq = "";
        if (position == 0) {
            seq = mContext.getString(R.string.tab_maps_title);
        } else if (position == 1) {
            seq = mContext.getString(R.string.list_location);
        }
        return seq;
    }

    @Override
    public void onLocationChange(Bundle bundle) {
        Log.i("callback", "hi from adapter");
        LocationListFragment frag = (LocationListFragment) mFrags.get(1);
        if (frag != null) {
            frag.onLocationChange(bundle);
        }
    }
}