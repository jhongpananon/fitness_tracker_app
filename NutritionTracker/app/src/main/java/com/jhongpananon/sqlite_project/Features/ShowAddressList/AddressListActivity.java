package com.jhongpananon.sqlite_project.Features.ShowAddressList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.Features.CreateAddress.Address;
import com.jhongpananon.sqlite_project.Features.CreateAddress.AddressCreateDialogFragment;
import com.jhongpananon.sqlite_project.Features.CreateAddress.AddressCreateListener;
import com.jhongpananon.sqlite_project.MapsActivity;
import com.jhongpananon.sqlite_project.R;
import com.jhongpananon.sqlite_project.Util.Config;
//import com.jhongpananon.sqlite_project.mapwithmarker.MapsMarkerActivity;
import com.jhongpananon.sqlite_project.graphFragment;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends AppCompatActivity implements AddressCreateListener{

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Address> addressList = new ArrayList<>();

    private TextView addressListEmptyTextView;
    private RecyclerView recyclerView;
    private AddressListRecyclerViewAdapter addressListRecyclerViewAdapter;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = (RecyclerView) findViewById(R.id.addressRecyclerView);
        addressListEmptyTextView = (TextView) findViewById(R.id.emptyAddressListTextView);

        addressList.addAll(databaseQueryClass.getAllAddress());

        addressListRecyclerViewAdapter = new AddressListRecyclerViewAdapter(this, addressList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(addressListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressCreateDialog();
            }
        });

        FloatingActionButton fab_graph = (FloatingActionButton) findViewById(R.id.fab_graph);
        fab_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressCreateDialog();
            }
        });

//        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openMapsActivity();
//            }
//        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);
//        mDrawerLayout = findViewById(R.id.nav_view);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        int id = menuItem.getItemId();
                        switch(id)
                        {
                            case R.id.nav_map: {
                                Log.i("main", "map clicked");
                                openMapsActivity();
                                finish();
//                                mDrawerLayout.closeDrawers();
                                break;
                            }
                            default:
                                return true;
                        }
                        return true;
                    }
                });
    }

    private void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure, You wanted to delete all addresss?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllAddresss();
                            if(isAllDeleted){
                                addressList.clear();
                                addressListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(addressList.isEmpty())
            addressListEmptyTextView.setVisibility(View.VISIBLE);
        else
            addressListEmptyTextView.setVisibility(View.GONE);
    }

    private void openAddressCreateDialog() {
        AddressCreateDialogFragment addressCreateDialogFragment = AddressCreateDialogFragment.newInstance("Add Workout", this);
        addressCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_ADDRESS);
    }

    @Override
    public void onAddressCreated(Address address) {
        addressList.add(address);
        addressListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(address.getName());
    }

}
