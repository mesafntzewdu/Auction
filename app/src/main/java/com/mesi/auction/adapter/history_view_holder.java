package com.mesi.auction.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mesi.auction.R;

public class history_view_holder extends RecyclerView.ViewHolder {


    TextView history_item_name;
    TextView history_item_by;
    TextView history_item_date;
    TextView history_av_rate;
    TextView history_price;


    public history_view_holder(@NonNull View itemView) {

        super(itemView);
        history_item_name = itemView.findViewById(R.id.history_item_name);
        history_item_by = itemView.findViewById(R.id.history_item_by);
        history_item_date = itemView.findViewById(R.id.history_item_date);
        history_av_rate = itemView.findViewById(R.id.history_av_rate);
        history_price = itemView.findViewById(R.id.history_price);

    }
}
