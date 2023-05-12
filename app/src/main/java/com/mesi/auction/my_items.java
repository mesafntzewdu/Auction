package com.mesi.auction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mesi.auction.adapter.member_item_adapter;
import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class my_items extends Fragment {

    ListView my_item_list;
    member_item_adapter adapter;
    DBHelper db;

    public my_items() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_items, container, false);
        db = new DBHelper(getContext());
        my_item_list = v.findViewById(R.id.my_item_list);

        String user_id;

        user_id = singleDAO.getSingleInstance().getUser_session();

        if (user_id.equals("empty")) {

            try {

                CSVReader reader = new CSVReader(new FileReader(Environment.getExternalStorageDirectory() + "/AUCTION/file/state.csv"));

                String[] next = reader.readNext();

                user_id = next[0];

                reader.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        }

        adapter = new member_item_adapter(getContext(), db.getMemberItem(user_id));

        my_item_list.setAdapter(adapter);

        my_item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.getsingleItem(adapterView.getAdapter().getItemId(i));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new member_item_detail()).commit();
            }
        });

        return v;
    }

}