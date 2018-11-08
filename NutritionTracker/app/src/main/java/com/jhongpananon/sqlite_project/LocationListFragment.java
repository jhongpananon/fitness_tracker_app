package com.jhongpananon.sqlite_project;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationListFragment extends Fragment implements MapFragment.LocationChangeListener {

    private MyRecyclerViewAdapter adapter;

    public LocationListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        Log.i("test", "hi");

        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop gets called");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume gets called");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause gets called");
    }

    @Override
    public void onLocationChange(Bundle bundle) {
        Log.i("callback", "hi from locationlistfrag");
        setRecycleView(bundle);
    }

    public void setRecycleView(Bundle bundle) {
        ArrayList<String> rows = new ArrayList<>();
        if (bundle != null) {
            ArrayList<String> names = bundle.getStringArrayList(MapFragment.LOC_NAME);
            ArrayList<String> addr = bundle.getStringArrayList(MapFragment.LOC_ADDR);
            ArrayList<String> attr = bundle.getStringArrayList(MapFragment.LOC_ATTR);
            ArrayList<LatLng> coord = bundle.getParcelableArrayList(MapFragment.LOC_COORD);
            ArrayList<String> rating = bundle.getStringArrayList(MapFragment.LOC_RATING);


            String row = "";

            for (int i = 0; i < names.size(); i++) {
                for (int j = 0; j < names.size(); j++) {
                    if (Float.valueOf(rating.get(j)) < Float.valueOf(rating.get(i))) {
                        Collections.swap(rating, i, j);
                        Collections.swap(names, i, j);
                        Collections.swap(addr, i, j);
                        Collections.swap(coord, i, j);
                        Collections.swap(attr, i, j);
                    }
                }
            }

            for (int i = 0; i < names.size(); i++) {
                row = "";
                row += "Rating: " + String.valueOf(rating.get(i)) + "\n" + names.get(i) + "\n"
                        + addr.get(i) + "\n"
                        + "Latitude: " + String.valueOf(coord.get(i).latitude)
                        + "\n" + "Longitude: " + String.valueOf(coord.get(i).longitude);

                rows.add(row);
                Log.i("name", names.get(i));
                Log.i("lat", String.valueOf(coord.get(i).latitude));
                Log.i("long", String.valueOf(coord.get(i).longitude));
                Log.i("Rating", String.valueOf(rating.get(i)));

            }
            Log.i("row size", String.valueOf(rows.size()));

            // set up the RecyclerView
            RecyclerView recyclerView = getActivity().findViewById(R.id.recycle_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

            adapter = new MyRecyclerViewAdapter(getActivity(), rows);
            adapter.notifyDataSetChanged();
            adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", adapter.getItem(position).split("\n", 4)[2]);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getContext(), "                                Copied: \n" + adapter.getItem(position).split("\n",4)[2], Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                }
            });

            recyclerView.setAdapter(adapter);

        }

    }
}
