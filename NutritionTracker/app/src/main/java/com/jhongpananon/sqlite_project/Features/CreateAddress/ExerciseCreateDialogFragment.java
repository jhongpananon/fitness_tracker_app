package com.jhongpananon.sqlite_project.Features.CreateAddress;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.R;
import com.jhongpananon.sqlite_project.Util.Config;

//import android.app.DialogFragment;


public class ExerciseCreateDialogFragment extends DialogFragment {

    private static ExerciseCreateListener exerciseCreateListener;

    private EditText nameEditText;
    private EditText repsEditText;
    private EditText dateEditText;
    private TextView exerciseNameView;
    private EditText weightEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
    private long repsNumber = -1;
    private long date = -1;
    private long set = -1;
    private double weight = -1.0;
    private String phoneString = "";
    private String emailString = "";

    public ExerciseCreateDialogFragment() {
        // Required empty public constructor
    }

    public static ExerciseCreateDialogFragment newInstance(String title, ExerciseCreateListener listener){
        exerciseCreateListener = listener;
        ExerciseCreateDialogFragment workoutCreateDialogFragment = new ExerciseCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        workoutCreateDialogFragment.setArguments(args);

        workoutCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return workoutCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nameString = getArguments().getString("exercise_name");
        date = getArguments().getLong("exercise_date");
        set = getArguments().getLong("num_sets");
        Log.d("add set", Long.toString(set));

        if(set <= 0)
        {
            set = 1;
        }
        else
        {
            set += 1;
        }
        View view = inflater.inflate(R.layout.fragment_exercise_create_dialog, container, false);

        nameEditText = view.findViewById(R.id.exerciseNameEditText);
        repsEditText = view.findViewById(R.id.repsEditText);
        dateEditText = view.findViewById(R.id.workoutDateEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        exerciseNameView = view.findViewById(R.id.exerciseNameView);
        weightEditText = view.findViewById(R.id.weightEditText);

        exerciseNameView.setText(nameString);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(repsEditText.getText().toString())) {
                    repsNumber = 1;
                }
                else {
                    repsNumber = Long.parseLong(repsEditText.getText().toString());
                }

                if("".equals(weightEditText.getText().toString()))
                {
                    weight = -1.0;
                }
                else
                {
                    weight =  Double.parseDouble(weightEditText.getText().toString());
                }

                Log.d("add repsNumber", Long.toString(repsNumber));
                Log.d("add nameString", nameString);
                Log.d("add date", Long.toString(date));
                Log.d("add set", Long.toString(set));

                Exercise exercise = new Exercise(-1, nameString, repsNumber, date, set, weight);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertAddress(exercise);

                if(id>0){
                    exercise.setId(id);
                    exerciseCreateListener.onExerciseCreated(exercise);
                    getDialog().dismiss();
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
