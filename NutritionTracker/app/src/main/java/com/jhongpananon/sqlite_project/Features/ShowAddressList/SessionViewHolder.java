package com.jhongpananon.sqlite_project.Features.ShowAddressList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.R;

public class SessionViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView registrationNumTextView;
    TextView registrationDate;
    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public SessionViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.exerciseTextView);
        registrationDate = itemView.findViewById(R.id.registrationDate);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
