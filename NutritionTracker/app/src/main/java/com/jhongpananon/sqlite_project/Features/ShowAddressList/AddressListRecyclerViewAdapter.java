package com.jhongpananon.sqlite_project.Features.ShowAddressList;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jhongpananon.sqlite_project.Database.DatabaseQueryClass;
import com.jhongpananon.sqlite_project.Features.CreateAddress.Exercise;
import com.jhongpananon.sqlite_project.Features.UpdateAddressInfo.AddressUpdateDialogFragment;
import com.jhongpananon.sqlite_project.Features.UpdateAddressInfo.AddressUpdateListener;
import com.jhongpananon.sqlite_project.R;
import com.jhongpananon.sqlite_project.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

public class AddressListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Exercise> exerciseList;
    private DatabaseQueryClass databaseQueryClass;

    public AddressListRecyclerViewAdapter(Context context, List<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Exercise exercise = exerciseList.get(position);

        holder.nameTextView.setText(exercise.getName());
        holder.registrationNumTextView.setText(String.valueOf(exercise.getRegistrationNumber()));
        holder.registrationDate.setText(String.valueOf(exercise.getDate()));

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this exercise?");
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        deleteAddress(itemPosition);
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
        });

        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressUpdateDialogFragment addressUpdateDialogFragment = AddressUpdateDialogFragment.newInstance(exercise.getRegistrationNumber(), itemPosition, new AddressUpdateListener() {
                    @Override
                    public void onAddressInfoUpdated(Exercise address, int position) {
                        exerciseList.set(position, address);
                        notifyDataSetChanged();
                    }
                });
                addressUpdateDialogFragment.show(((AddressListActivity) context).getSupportFragmentManager(), Config.UPDATE_ADDRESS);
            }
        });
    }

    private void deleteAddress(int position) {
        Exercise exercise = exerciseList.get(position);
        long count = databaseQueryClass.deleteAddressByRegNum(exercise.getRegistrationNumber());

        if(count>0){
            exerciseList.remove(position);
            notifyDataSetChanged();
            ((AddressListActivity) context).viewVisibility();
            Toast.makeText(context, "Exercise deleted successfully", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Exercise not deleted. Something wrong!", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
