package com.jhongpananon.sqlite_project.Features.CreateAddress;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jhongpananon.sqlite_project.Util.Config;
import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.R;


public class AddressCreateDialogFragment extends DialogFragment {

    private static AddressCreateListener addressCreateListener;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private String phoneString = "";
    private String emailString = "";

    public AddressCreateDialogFragment() {
        // Required empty public constructor
    }

    public static AddressCreateDialogFragment newInstance(String title, AddressCreateListener listener){
        addressCreateListener = listener;
        AddressCreateDialogFragment addressCreateDialogFragment = new AddressCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        addressCreateDialogFragment.setArguments(args);

        addressCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return addressCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address_create_dialog, container, false);

        nameEditText = view.findViewById(R.id.addressNameEditText);
        registrationEditText = view.findViewById(R.id.registrationEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = nameEditText.getText().toString();
                registrationNumber = Integer.parseInt(registrationEditText.getText().toString());

                Address address = new Address(-1, nameString, registrationNumber);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertAddress(address);

                if(id>0){
                    address.setId(id);
                    addressCreateListener.onAddressCreated(address);
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
