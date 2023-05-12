package com.mesi.auction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mesi.auction.R;
import com.mesi.auction.dao.ItemDAO;
import java.util.List;

public class bid_history_adapter extends RecyclerView.Adapter<history_view_holder> {


    private List<ItemDAO> list;

    public bid_history_adapter(List<ItemDAO> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public history_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_history_list_view, parent, false);

        return new history_view_holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull history_view_holder holder, int i) {

        holder.history_item_name.setText(list.get(i).getItem_name());
        holder.history_item_by.setText(list.get(i).getItem_bid_by());
        holder.history_item_date.setText(list.get(i).getItem_bid_date());
        holder.history_av_rate.setText(list.get(i).getItem_rate_value());
        holder.history_price.setText(list.get(i).getItem_bid_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
