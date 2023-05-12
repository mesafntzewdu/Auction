package com.mesi.auction.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mesi.auction.R;
import com.mesi.auction.dao.DAO;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class bought_item_adapter  extends BaseAdapter {

    List<DAO> itemList;
    Context mContext;

    public bought_item_adapter(Context context, List<DAO> itemList)
    {
        this.itemList = itemList;
        mContext =  context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itemList.get(i).getItem_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(mContext, R.layout.bought_item_list, null);

        TextView list_item_name = v.findViewById(R.id.list_item_name);
        TextView list_day_left = v.findViewById(R.id.list_day_left);
        ImageView list_item_image = v.findViewById(R.id.list_item_image);

        list_item_name.setText(itemList.get(i).getItem_name());
        list_day_left.setText(itemList.get(i).getEnd_date());
        try{
            Picasso.get().load(new File(itemList.get(i).getImg_path())).into(list_item_image);
        }catch (Exception ignored)
        {

        }

        return v;
    }

//    public bought_item_adapter(List<DAO> itemList)
//    {
//        this.itemList = itemList;
//    }
//
//    @NonNull
//    @Override
//    public bought_item_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bought_item_list, parent, false);
//
//        return new bought_item_view_holder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull bought_item_view_holder holder, int i) {
//
//        holder.list_item_name.setText(itemList.get(i).getItem_name());
//        holder.list_day_left.setText(itemList.get(i).getEnd_date());
//        try{
//            Picasso.get().load(new File(itemList.get(i).getImg_path())).into(holder.list_item_image);
//        }catch (Exception ignored)
//        {
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return itemList.size();
//    }
}
