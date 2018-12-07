package com.jhongpananon.sqlite_project.Features.CreateAddress;

import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.Features.ShowAddressList.SessionListActivity;
import com.jhongpananon.sqlite_project.SelectDateFragment;
import com.jhongpananon.sqlite_project.Util.Config;
import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.R;

import java.util.ArrayList;
import java.util.List;


public class WorkoutCreateDialogFragment extends DialogFragment {

    private static WorkoutCreateListener workoutCreateListener;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText dateEditText;
    private Button createButton;
    private Button cancelButton;
    private List<Exercise> exerciseList = new ArrayList<>();

    private String nameString = "`";
    private long registrationNumber = -1;
    private long date = -1;
    private long set = -1;
    private double weight = -1.0;
    private String phoneString = "";
    private String emailString = "";

    private boolean uniqueEntry = true;

    public WorkoutCreateDialogFragment() {
        // Required empty public constructor
    }

    public static WorkoutCreateDialogFragment newInstance(String title, WorkoutCreateListener listener){
        workoutCreateListener = listener;
        WorkoutCreateDialogFragment workoutCreateDialogFragment = new WorkoutCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        workoutCreateDialogFragment.setArguments(args);

        workoutCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return workoutCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());
        exerciseList.addAll(databaseQueryClass.getAllExercises());

        date = getArguments().getLong("date");
        View view = inflater.inflate(R.layout.fragment_workout_create_dialog, container, false);

        nameEditText = view.findViewById(R.id.exerciseNameEditText);
        registrationEditText = view.findViewById(R.id.repsEditText);
        dateEditText = view.findViewById(R.id.workoutDateEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = Long.parseLong(dateEditText.getText().toString());

                for(int i = 0; i < exerciseList.size(); i++)
                {
                    if(exerciseList.get(i).getDate() == date)
                    {
                        uniqueEntry = false;
                    }
                }

                if(true)
                {
                    Exercise exercise = new Exercise(-1, nameString, registrationNumber, date, set, weight);

                    DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                    long id = databaseQueryClass.insertWorkout(exercise);

                    if(id>0){
                        exercise.setId(id);
                        workoutCreateListener.onWorkoutCreated(exercise);
                        getDialog().dismiss();
                    }
                    else
                    {
                        getDialog().dismiss();
                        Intent intent = new Intent(view.getContext(), SessionListActivity.class);
                        intent.putExtra("date", date);
                        view.getContext().startActivity(intent);
                    }
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }


}
