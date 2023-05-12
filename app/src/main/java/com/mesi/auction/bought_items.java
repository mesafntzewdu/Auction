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

import com.mesi.auction.adapter.bought_item_adapter;
import com.mesi.auction.dao.DAO;
import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class bought_items extends Fragment {
    DBHelper db;
    bought_item_adapter adapter;
    ListView rv;

    public bought_items() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bought_items, container, false);

        rv = v.findViewById(R.id.bought_item_list);
        db = new DBHelper(getContext());

        String[] user_id = new String[2];

        if (singleDAO.getSingleInstance().getUser_session().equals("empty")) {
            try {
                CSVReader reader = new CSVReader(new FileReader(Environment.getExternalStorageDirectory() + "/AUCTION/file/state.csv"));

                user_id = reader.readNext();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            user_id[0] = singleDAO.getSingleInstance().getUser_session();
        }

        adapter = new bought_item_adapter(getContext(), db.getAllBoughtItemForUser(user_id[0]));

        rv.setAdapter(adapter);

        rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  db.getBoughtItemDetailSingleItem(adapterView.getAdapter().getItemId(i));
                  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new bought_item_detail()).commit();
            }
        });

        return v;
    }
}