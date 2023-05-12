package com.mesi.auction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.mesi.auction.adapter.list_adapter;
import com.mesi.auction.dao.DAO;
import com.mesi.auction.database.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class admin_approval extends Fragment {

    ListView list_items;
    List<DAO> list;
    EditText searchItem;
    DBHelper db;
    list_adapter adapter;
    Spinner member_category;

    public admin_approval() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_approval, container, false);


        db = new DBHelper(getContext());
        list_items = v.findViewById(R.id.list_items);

        homeListLoader();

        return v;
    }


    private synchronized void homeListLoader() {

        adapter = new list_adapter(getContext(), db.getAllItemsPending());
        list_items.setAdapter(adapter);

        list_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                db.getsingleItem(adapterView.getAdapter().getItemId(i));

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new admin_approval_detail()).commit();
            }
        });
    }
}