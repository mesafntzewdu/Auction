package com.mesi.auction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class no_item_member_notify extends Fragment {

 Button go_to_sell_tab;
    public no_item_member_notify() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_no_item_member_notify, container, false);

        go_to_sell_tab = v.findViewById(R.id.go_to_sell_tab);

        go_to_sell_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new sell_item()).commit();
            }
        });
        return v;
    }
}