package com.jhongpananon.sqlite_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.Features.CreateAddress.Exercise;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class displayGraph extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<>();
    private List<Exercise> filteredByName = new ArrayList<>();
    private List<Exercise> filteredByWeight = new ArrayList<>();
    private List<Exercise> exerciseListByName = new ArrayList<>();
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);
    private String exerciseName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        exerciseName = getIntent().getStringExtra("exercise_name");
        Log.i("exerciseName", exerciseName);
        super.onCreate(savedInstanceState);
        exerciseList.addAll(databaseQueryClass.getAllExercises());
        exerciseListByName.addAll(databaseQueryClass.getExerciseByName(exerciseName));
        Log.i("exerciseListByName", Long.toString(exerciseListByName.size()));


        Collections.sort(exerciseListByName, new CustomComparator());

        double maxWeight = 0.0;
        long prevDate = 0;
        int saveIndex = 0;
        Exercise exercise = exerciseListByName.get(0);
        Exercise prevExercise = exerciseListByName.get(0);



        for(int i = 0; i < exerciseListByName.size(); i++)
        {
            exercise = exerciseListByName.get(i);
            if(exercise.getDate() != prevExercise.getDate())
            {
                filteredByWeight.add(exerciseListByName.get(saveIndex));
                saveIndex = i;
                prevExercise = exercise;
                maxWeight = 0.0;
            }
            else
            {
                if(exercise.getWeight() > maxWeight)
                {
                    saveIndex = i;
                }
            }
            if(i == exerciseListByName.size() - 1)
            {
                filteredByWeight.add(exerciseListByName.get(saveIndex));
            }
        }


        setContentView(R.layout.activity_display_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        Collections.sort(filteredByWeight, new CustomComparator());

        LineGraphSeries<DataPoint> series = new LineGraphSeries();
        long maxDate = 0;
        for(int i = 0; i < filteredByWeight.size(); i++)
        {
            Log.i("num exercises", Integer.toString(filteredByWeight.size()));
            Log.i("exercise", Integer.toString(i));
            Log.i("exercise reps", Long.toString(filteredByWeight.get(i).getRegistrationNumber()));
            Log.i("exercise date", Long.toString(filteredByWeight.get(i).getDate()));
            Log.i("exercise weight", Double.toString(filteredByWeight.get(i).getWeight()));
            if(filteredByWeight.get(i).getDate() > maxDate)
            {
                maxDate = filteredByWeight.get(i).getDate();
            }
            series.appendData(new DataPoint((double)filteredByWeight.get(i).getDate(), filteredByWeight.get(i).getWeight()), true, 10, true);
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);

        series.setDrawDataPoints(true);
        double max_x = (double)maxDate; // or max(datapoints.x)
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(max_x);
//        graph.getViewport().setScalable(true);
//        graph.getViewport().setScalableY(true);
        graph.addSeries(series);
    }

    public static class CustomComparator implements Comparator<Exercise> {
        @Override
        public int compare(Exercise o1, Exercise o2) {
            int retval = 0;
            if (o1.getDate() < o2.getDate())
            {
                retval = -1;
            }
            else if (o1.getDate() > o2.getDate())
            {
                retval = 1;
            }
            return retval;
        }
    }

    public void swapMax(Exercise exercise)
    {

    }

}
