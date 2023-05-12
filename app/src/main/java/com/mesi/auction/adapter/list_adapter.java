package com.mesi.auction.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mesi.auction.R;
import com.mesi.auction.dao.DAO;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class list_adapter extends BaseAdapter {

    private Context context;
    private List<DAO> list;

    public list_adapter(Context context, List<DAO> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getItem_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.item_list_view, null);

        TextView item_name = v.findViewById(R.id.list_item_name);
        TextView list_day_left = v.findViewById(R.id.list_day_left);
        ImageView item_img = v.findViewById(R.id.list_item_image);
        ImageView item_sold = v.findViewById(R.id.list_item_sold);

        if (list.get(i).getSold_item_id()>0)
        {
            item_sold.setImageResource(R.drawable.sold);
        }else
        {
            item_sold.setImageResource(R.color.white);
        }

        item_name.setText(list.get(i).getItem_name());
        
        if (getCount() == 0) {

        }
        list_day_left.setText(list.get(i).getEnd_date());
        Picasso.get().load(new File(list.get(i).getImg_path())).resize(50, 50).into(item_img);

        return v;
    }
}
