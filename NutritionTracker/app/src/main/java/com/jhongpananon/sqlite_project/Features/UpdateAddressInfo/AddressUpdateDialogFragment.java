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
import com.jhongpananon.sqlite_project.Features.CreateAddress.Address;
import com.jhongpananon.sqlite_project.R;
import com.jhongpananon.sqlite_project.Util.Config;


public class AddressUpdateDialogFragment extends DialogFragment {

    private static long addressRegNo;
    private static int addressItemPosition;
    private static AddressUpdateListener addressUpdateListener;

    private Address mAddress;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText dateEditText;
    private EditText emailEditText;
    private Button updateButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private long date = -1;
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

        nameEditText = view.findViewById(R.id.addressNameEditText);
        registrationEditText = view.findViewById(R.id.registrationEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        updateButton = view.findViewById(R.id.updateAddressInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mAddress = databaseQueryClass.getAddressByRegNum(addressRegNo);

        if(mAddress!=null){
            nameEditText.setText(mAddress.getName());
            registrationEditText.setText(String.valueOf(mAddress.getRegistrationNumber()));
            dateEditText.setText(String.valueOf(mAddress.getDate()));

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

                    mAddress.setName(nameString);
                    mAddress.setRegistrationNumber(registrationNumber);
                    mAddress.setDate(date);

                    long id = databaseQueryClass.updateAddressInfo(mAddress);

                    if(id>0){
                        addressUpdateListener.onAddressInfoUpdated(mAddress, addressItemPosition);
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
