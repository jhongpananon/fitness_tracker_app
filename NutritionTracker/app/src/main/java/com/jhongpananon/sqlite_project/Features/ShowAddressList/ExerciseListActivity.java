package com.jhongpananon.sqlite_project.Features.ShowAddressList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.Features.CreateAddress.ExerciseCreateDialogFragment;
import com.jhongpananon.sqlite_project.Features.CreateAddress.ExerciseCreateListener;
import com.jhongpananon.sqlite_project.Features.CreateAddress.WorkoutCreateDialogFragment;
import com.jhongpananon.sqlite_project.Features.CreateAddress.WorkoutCreateListener;
import com.jhongpananon.sqlite_project.Features.CreateAddress.Exercise;
import com.jhongpananon.sqlite_project.IntentHelper;
import com.jhongpananon.sqlite_project.MapsActivity;
import com.jhongpananon.sqlite_project.R;
import com.jhongpananon.sqlite_project.Util.Config;
import com.jhongpananon.sqlite_project.displayGraph;
import com.jhongpananon.sqlite_project.graphFragment;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import android.support.v7.app.AlertDialog;
//import com.jhongpananon.sqlite_project.mapwithmarker.MapsMarkerActivity;

public class ExerciseListActivity extends AppCompatActivity implements ExerciseCreateListener {

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Exercise> exerciseList = new ArrayList<>();

    private TextView addressListEmptyTextView;
    private RecyclerView recyclerView;
    private ExerciseListRecyclerViewAdapter exerciseListRecyclerViewAdapter;

    private DrawerLayout mDrawerLayout;
    private List<Exercise> filteredExerciseList = new ArrayList<>();
    Long exercise_date;
    String exercise_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        exercise_date = getIntent().getLongExtra("exercise_date", 0);
        exercise_name = getIntent().getStringExtra("exercise_name");
        Log.d("exercise_date", Long.toString(exercise_date));
        Log.d("exercise_name", exercise_name);
        String exerciseNameTitle = exercise_name.toLowerCase();
        exerciseNameTitle = exerciseNameTitle.substring(0, 1).toUpperCase() + exerciseNameTitle.substring(1);
        this.setTitle(exerciseNameTitle);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        addressListEmptyTextView = (TextView) findViewById(R.id.emptyAddressListTextView);
        recyclerView = (RecyclerView) findViewById(R.id.addressRecyclerView);

        exerciseList.addAll(databaseQueryClass.getAllExercises());

        for(Integer i = 0; i < exerciseList.size(); i++)
        {
            if(exerciseList.get(i).getDate() == exercise_date)
            {
                Log.d("inside get date", Long.toString(exerciseList.get(i).getDate()));
                if(exercise_name.equals(exerciseList.get(i).getName()))
                {
                    Log.d("inside name equal", exercise_name);
                    Log.d("sets", Long.toString(exerciseList.get(i).getSet()));
                    Log.d("getRegistrationNumber", Long.toString(exerciseList.get(i).getRegistrationNumber()));
                    if((exerciseList.get(i).getSet() > 0) && (exerciseList.get(i).getRegistrationNumber() > 0))
                    {

                        filteredExerciseList.add(exerciseList.get(i));
                    }
                }
            }
        }

        Collections.sort(filteredExerciseList, new displayGraph.CustomComparator());
        exerciseListRecyclerViewAdapter = new ExerciseListRecyclerViewAdapter(this, filteredExerciseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(exerciseListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressCreateDialog();
            }
        });

//        FloatingActionButton fab_graph = (FloatingActionButton) findViewById(R.id.fab_graph);
//        fab_graph.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openGraphActivity();
//            }
//        });

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
        IntentHelper.addObjectForKey(databaseQueryClass, "key");
        startActivity(intent);
    }

    private void openGraphActivity() {
        Intent intent = new Intent(this, displayGraph.class);
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
                                exerciseList.clear();
                                exerciseListRecyclerViewAdapter.notifyDataSetChanged();
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
        if(exerciseList.isEmpty())
            addressListEmptyTextView.setVisibility(View.VISIBLE);
        else
            addressListEmptyTextView.setVisibility(View.GONE);
    }

    private void openAddressCreateDialog() {
        Bundle bundle = new Bundle();
        bundle.putLong("exercise_date", exercise_date);
        bundle.putString("exercise_name", exercise_name);
        bundle.putLong("num_sets", filteredExerciseList.size());
        ExerciseCreateDialogFragment exerciseCreateDialogFragment = ExerciseCreateDialogFragment.newInstance("Add Workout", this);
        exerciseCreateDialogFragment.setArguments(bundle);
        exerciseCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_ADDRESS);
    }


    private void openGraphFragment() {
        Bundle bundle=new Bundle();
        graphFragment graph = new graphFragment();
        graph.setArguments(bundle);
    }


    @Override
    public void onExerciseCreated(Exercise exercise) {
        exerciseList.add(exercise);
        exerciseListRecyclerViewAdapter.notifyItemInserted(exerciseList.size());
        exerciseListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(exercise.getName());
    }

}
