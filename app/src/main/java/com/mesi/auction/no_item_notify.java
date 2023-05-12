package com.mesi.auction;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class no_item_notify extends Fragment {


    Button subscribe_for_item;
    Button register_no_item_page;
    ImageView go_back;



    public no_item_notify() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_no_item_notify, container, false);
        subscribe_for_item = v.findViewById(R.id.subscribe_for_item);
        register_no_item_page = v.findViewById(R.id.register_no_item_page);
        go_back = v.findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), visitorsActivity.class);
                startActivity(i);
            }
        });

        subscribe_for_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new subscribe_for_item()).commit();
            }
        });

        register_no_item_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new register()).commit();
            }
        });

        return v;
    }
}