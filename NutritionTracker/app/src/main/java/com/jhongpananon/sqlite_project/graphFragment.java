package com.jhongpananon.sqlite_project;

import android.app.Dialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

//import android.support.v4.app.DialogFragment;


public class graphFragment extends DialogFragment {

    private static GraphCreateListener graphCreateListener;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText dateEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private long date = -1;
    private String phoneString = "";
    private String emailString = "";

    public graphFragment() {
        // Required empty public constructor
    }

//    public static graphFragment newInstance(String title, GraphCreateListener listener){
//        graphCreateListener = listener;
//        graphFragment graphCreateDialogFragment = new graphFragment();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        graphFragment.setArguments(args);
//
//        graphFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
//
//        return graphFragment;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.graph_fragment_layout, container, false);

//        nameEditText = view.findViewById(R.id.addressNameEditText);
//        registrationEditText = view.findViewById(R.id.registrationEditText);
//        dateEditText = view.findViewById(R.id.dateEditText);
//        createButton = view.findViewById(R.id.createButton);
//        cancelButton = view.findViewById(R.id.cancelButton);
//
//        String title = getArguments().getString(Config.TITLE);
//        getDialog().setTitle(title);
//
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nameString = nameEditText.getText().toString();
//                if ("".equals(registrationEditText.getText().toString())) {
//                    registrationNumber = 1;
//                }
//                else {
//                    registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
//                }
//                date = Integer.parseInt(dateEditText.getText().toString());
//
//                Exercise address = new Exercise(-1, nameString, registrationNumber, date);
//
//                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());
//
//                long id = databaseQueryClass.insertAddress(address);
//
//                if(id>0){
//                    address.setId(id);
//                    addressCreateListener.onAddressCreated(address);
//                    getDialog().dismiss();
//                }
//            }
//        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);


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
