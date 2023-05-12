package com.mesi.auction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mesi.auction.adapter.list_adapter;
import com.mesi.auction.dao.DAO;
import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class List_items extends Fragment {
    DBHelper db;
    ListView lv;
    List<DAO> itemList;
    List<DAO> filterList;
    list_adapter adapter;
    EditText searchItem;

    public List_items() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_items, container, false);

        searchItem = v.findViewById(R.id.searchItem);

        filterList = new ArrayList<>();

        itemList = new ArrayList<>();

        db = new DBHelper(getContext());

        listItemsMethod(v);

        return v;
    }

    private void searchFromItem(String generalCat) {

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchFilter(s.toString().trim(), generalCat);

            }
        });

    }

    private void listItemsMethod(View v) {
        lv = v.findViewById(R.id.list_items);

        categoryFilter(singleDAO.getSingleInstance().getGeneralCat());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                db.getsingleItem(adapterView.getAdapter().getItemId(i));

                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new item_detail()).commit();
            }
        });

        searchFromItem(singleDAO.getSingleInstance().getGeneralCat());
    }


    private void searchFilter(String val, String generalCat) {
        //All users are listed in
        ArrayList<DAO> items = db.getAllItems();
        ArrayList<DAO> filterItem = new ArrayList<>();

        if (val.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {

                if (items.get(i).getCategory().equalsIgnoreCase(generalCat)) {
                    filterItem.add(items.get(i));
                }
            }


        } else {


            for (int i = 0; i < items.size(); i++) {

                if (items.get(i).getItem_name().toLowerCase().contains(val.toLowerCase()) && items.get(i).getCategory().equalsIgnoreCase(generalCat)) {
                    filterItem.add(items.get(i));
                }
            }


        }

        adapter = new list_adapter(getContext(), filterItem);
        lv.setAdapter(adapter);
        lv.deferNotifyDataSetChanged();
    }

    private void categoryFilter(String val) {
        //All users are listed in
        ArrayList<DAO> items = db.getAllItems();

        ArrayList<DAO> filterItem = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).getCategory().toLowerCase().contains(val.toLowerCase())) {
                filterItem.add(items.get(i));
            }
        }

        if (filterItem.size() <= 0) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new no_item_notify()).commit();
        }

        adapter = new list_adapter(getContext(), filterItem);
        lv.setAdapter(adapter);
        lv.deferNotifyDataSetChanged();
    }

}