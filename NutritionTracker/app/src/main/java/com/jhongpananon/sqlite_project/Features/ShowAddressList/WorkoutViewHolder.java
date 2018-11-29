package com.jhongpananon.sqlite_project.Features.ShowAddressList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.R;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {

    TextView registrationDate;
    ImageView crossButtonImageView;

    public WorkoutViewHolder(View itemView) {
        super(itemView);

        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        registrationDate = itemView.findViewById(R.id.registrationDate);
    }
}
