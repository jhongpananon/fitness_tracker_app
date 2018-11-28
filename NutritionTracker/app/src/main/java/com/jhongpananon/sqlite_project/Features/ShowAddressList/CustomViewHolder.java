package com.jhongpananon.sqlite_project.Features.ShowAddressList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhongpananon.sqlite_project.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView registrationNumTextView;
    TextView registrationDate;
    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public CustomViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.nameTextView);
        registrationNumTextView = itemView.findViewById(R.id.registrationNumTextView);
        registrationDate = itemView.findViewById(R.id.registrationDate);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
