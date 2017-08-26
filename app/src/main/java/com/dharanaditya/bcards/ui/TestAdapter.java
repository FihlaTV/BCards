package com.dharanaditya.bcards.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dharanaditya.bcards.R;
import com.dharanaditya.bcards.model.BCard;

import java.util.List;

/**
 * Created by dharan1011 on 26/8/17.
 */

public class TestAdapter extends RecyclerView.Adapter<BCardViewHolder> {
    private List<BCard> bCards;
    private Context context;

    public TestAdapter(List<BCard> bCards, Context context) {
        this.bCards = bCards;
        this.context = context;
    }

    @Override
    public BCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bcard_list_item, parent, false);
        return new BCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BCardViewHolder holder, int position) {
        BCard card = bCards.get(position);
        holder.bind(card.getFirstName(), card.getLastName(), card.getEmailAddress(), card.getHeadline(), card.getPictureUrl());
    }

    @Override
    public int getItemCount() {
        return (bCards != null) ? bCards.size() : 0;
    }
}
