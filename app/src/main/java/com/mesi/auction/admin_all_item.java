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

public class admin_all_item extends Fragment {

    ListView list_items;
    List<DAO> list;
    EditText searchItem;
    DBHelper db;
    list_adapter adapter;
    Spinner member_category;

    public admin_all_item() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_all_item, container, false);
        db = new DBHelper(getContext());
        searchItem = v.findViewById(R.id.searchItem);
        list_items = v.findViewById(R.id.list_items);
        member_category = v.findViewById(R.id.member_category);

        try {
            checkIfItemDateIsNotOver();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        spinnerArrayFetch();

        searchFromItem();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                homeListLoader();
            }
        }, 200);

        return v;
    }

    //checking if the item dead time is over or not
    private void checkIfItemDateIsNotOver() throws ParseException {

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat endDate = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();

        Date start = currentDate.parse(currentDate.format(cal.getTime()));

        list = db.getAllItems();

        for (int i = 0; i < list.size(); i++) {

            assert start != null;
            if (start.after(endDate.parse(list.get(i).getEnd_date()))) {
                //Sell the item to the highest price bidder
                //And remove item from the, item for sell list

                db.insertSoldInfo(list.get(i).getItem_id());

            }

        }
    }


    private synchronized void spinnerArrayFetch() {

        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(getContext(), R.array.category, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_category.setAdapter(catAdapter);

        member_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryFilter(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private synchronized void homeListLoader() {

        adapter = new list_adapter(getContext(), db.getAllItems());
        list_items.setAdapter(adapter);

        list_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                db.getsingleItem(adapterView.getAdapter().getItemId(i));

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new admin_item_detail()).commit();
            }
        });
    }

    private void searchFromItem() {

        String regex = "^(?=.*)[d@$!%*?&]$";

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter(s.toString().trim());
            }
        });

    }


    private void searchFilter(String val) {
        //All users are listed in
        ArrayList<DAO> items = db.getAllItems();

        ArrayList<DAO> filterItem = new ArrayList<>();

        if (val.isEmpty())
        {
            for (int i = 0; i < items.size(); i++) {

                if (items.get(i).getItem_name().toLowerCase().contains(val.toLowerCase())) {
                    filterItem.add(items.get(i));
                }
            }
        }else
        {
            for (int i = 0; i < items.size(); i++) {

                if (items.get(i).getItem_name().toLowerCase().contains(val.toLowerCase())) {
                    filterItem.add(items.get(i));
                }
            }
        }


        adapter = new list_adapter(getContext(), filterItem);
        list_items.setAdapter(adapter);
        list_items.deferNotifyDataSetChanged();
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

        adapter = new list_adapter(getContext(), filterItem);
        list_items.setAdapter(adapter);
        list_items.deferNotifyDataSetChanged();
    }
}