package com.jhongpananon.sqlite_project.Features.UpdateAddressInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.Features.CreateAddress.Exercise;
import com.jhongpananon.sqlite_project.R;
import com.jhongpananon.sqlite_project.Util.Config;


public class AddressUpdateDialogFragment extends DialogFragment {

    private static long addressRegNo;
    private static int addressItemPosition;
    private static AddressUpdateListener addressUpdateListener;

    private Exercise mExercise;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText dateEditText;
    private EditText setEditText;
    private EditText weightEditText;
    private Button updateButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private long date = -1;
    private long set = -1;
    private long weight = -1;
    private String phoneString = "";
    private String emailString = "";

    private DatabaseQueryClass databaseQueryClass;

    public AddressUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static AddressUpdateDialogFragment newInstance(long registrationNumber, int position, AddressUpdateListener listener){
        addressRegNo = registrationNumber;
        addressItemPosition = position;
        addressUpdateListener = listener;
        AddressUpdateDialogFragment addressUpdateDialogFragment = new AddressUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update address information");
        addressUpdateDialogFragment.setArguments(args);

        addressUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return addressUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        nameEditText = view.findViewById(R.id.exerciseNameEditText);
        registrationEditText = view.findViewById(R.id.repsEditText);
        dateEditText = view.findViewById(R.id.workoutDateEditText);
        updateButton = view.findViewById(R.id.updateAddressInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        weightEditText = view.findViewById((R.id.weightEditText));

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mExercise = databaseQueryClass.getAddressByRegNum(addressRegNo);

        if(mExercise !=null){
            nameEditText.setText(mExercise.getName());
            registrationEditText.setText(String.valueOf(mExercise.getRegistrationNumber()));
            dateEditText.setText(String.valueOf(mExercise.getDate()));
            weightEditText.setText(String.valueOf(mExercise.getWeight()));

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = nameEditText.getText().toString();
                    if ("".equals(registrationEditText.getText().toString())) {
                        registrationNumber = 1;
                    }
                    else {
                        registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
                    }
                    date = Integer.parseInt(dateEditText.getText().toString());
                    weight = Long.parseLong(weightEditText.getText().toString());

                    mExercise.setName(nameString);
                    mExercise.setRegistrationNumber(registrationNumber);
                    mExercise.setDate(date);
                    mExercise.setSet(-1);
                    mExercise.setWeight(weight);

                    long id = databaseQueryClass.updateAddressInfo(mExercise);

                    if(id>0){
                        addressUpdateListener.onAddressInfoUpdated(mExercise, addressItemPosition);
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

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

}
