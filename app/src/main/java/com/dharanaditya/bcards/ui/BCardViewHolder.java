package com.dharanaditya.bcards.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dharanaditya.bcards.R;
import com.squareup.picasso.Picasso;

/**
 * Created by dharan1011 on 26/8/17.
 */

public class BCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView profileImageView;
    private TextView fullNameTextView, emailTextView, headlineTextView;

    public BCardViewHolder(View itemView) {
        super(itemView);
        profileImageView = (ImageView) itemView.findViewById(R.id.imv_profile_image);
        fullNameTextView = (TextView) itemView.findViewById(R.id.tv_profile_name);
        emailTextView = (TextView) itemView.findViewById(R.id.tv_profile_email);
        headlineTextView = (TextView) itemView.findViewById(R.id.tv_profile_headline);
    }

    public void bind(String firstName, String lastName, String email, String headLine, String imageUrl) {
        if (!imageUrl.isEmpty())
            Picasso.with(itemView.getContext()).load(imageUrl).into(profileImageView);
        fullNameTextView.setText(firstName + " " + lastName);
        emailTextView.setText(email);
        headlineTextView.setText(headLine);
    }

    @Override
    public void onClick(View view) {

    }
}
