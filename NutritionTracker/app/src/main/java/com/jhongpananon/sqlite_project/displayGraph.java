package com.jhongpananon.sqlite_project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.Features.CreateAddress.Exercise;
import com.jhongpananon.sqlite_project.R;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class displayGraph extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<>();
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exerciseList.addAll(databaseQueryClass.getAllExercises());


        setContentView(R.layout.activity_display_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getDialog().dismiss();
//            }
//        });


        Collections.sort(exerciseList, new CustomComparator());

        LineGraphSeries<DataPoint> series = new LineGraphSeries();
        long maxDate = 0;
        for(int i = 0; i < exerciseList.size(); i++)
        {
            Log.i("num exercises", Integer.toString(exerciseList.size()));
            Log.i("exercise", Integer.toString(i));
            Log.i("exercise", Long.toString(exerciseList.get(i).getRegistrationNumber()));
            Log.i("exercise date", Long.toString(exerciseList.get(i).getDate()));
            if(exerciseList.get(i).getDate() > maxDate)
            {
                maxDate = exerciseList.get(i).getDate();
            }
            series.appendData(new DataPoint((double)exerciseList.get(i).getDate(), (double)exerciseList.get(i).getRegistrationNumber()), true, 10, true);
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

    private class CustomComparator implements Comparator<Exercise> {
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

}
