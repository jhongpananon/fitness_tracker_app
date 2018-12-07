package com.jhongpananon.sqlite_project.Features.ShowAddressList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.R;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView weightTextView;
    TextView repsTextView;
    TextView setTextView;
    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public ExerciseViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.exerciseTextView);
        weightTextView = itemView.findViewById(R.id.weightTextView);
        setTextView = itemView.findViewById(R.id.setTextView);
        repsTextView = itemView.findViewById(R.id.repsTextView);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
