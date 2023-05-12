package com.mesi.auction.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mesi.auction.R;
import com.mesi.auction.dao.member_item_dao;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class member_item_adapter extends BaseAdapter {
    private List<member_item_dao> list;
    private Context mContext;

    public member_item_adapter(Context context, List<member_item_dao> memberItem) {

        this.mContext = context;
        this.list = memberItem;

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


        View v = View.inflate(mContext, R.layout.member_item_list_layout, null);

        TextView member_item_name = v.findViewById(R.id.member_item_name);
        TextView member_item_state = v.findViewById(R.id.member_item_state);
        ImageView member_item_image = v.findViewById(R.id.member_item_image);

        member_item_name.setText(list.get(i).getItem_name());

        if (list.get(i).getItem_state().equals("Approved")) {
            member_item_state.setTextColor(Color.GREEN);
            member_item_state.setText(list.get(i).getItem_state());
        } else {
            member_item_state.setTextColor(Color.RED);
            member_item_state.setText(list.get(i).getItem_state());
        }


        Picasso.get().load(new File(list.get(i).getItem_img())).into(member_item_image);


        return v;
    }
}
