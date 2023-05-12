package com.mesi.auction;

import android.graphics.BitmapFactory;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mesi.auction.adapter.bid_history_adapter;
import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVReader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileReader;

public class item_detail extends Fragment {

    ImageView detail_image;
    TextView item_name;
    TextView item_description;
    TextView end_date;
    TextView start_date;
    TextView item_price;
    TextView item_owner;
    RatingBar rating;

    RecyclerView bidders_list;
    bid_history_adapter adapter;

    DBHelper db;
    Button bid_now;

    public item_detail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        rating = v.findViewById(R.id.rating);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if ((new File(Environment.getExternalStorageDirectory() + "/AUCTION/file/state.csv").exists()) || !singleDAO.getSingleInstance().getUser_session().equals("empty")) {
                    updateWhenRateChange(ratingBar.getRating());
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new login()).commit();
                }

            }
        });


        db = new DBHelper(getContext());

        setZdiscription(v);

        return v;
    }

    private void updateWhenRateChange(float rating) {


        if (db.checkIfUserRate(String.valueOf(singleDAO.getSingleInstance().getItem_id()), singleDAO.getSingleInstance().getUserIdSession())) {
            db.updateRate(rating, singleDAO.getSingleInstance().getItem_id(), singleDAO.getSingleInstance().getUserIdSession());
        } else {
            db.insertRate(rating, singleDAO.getSingleInstance().getItem_id(), singleDAO.getSingleInstance().getUserIdSession());
        }

    }

    private void setZdiscription(View v) {

        detail_image = v.findViewById(R.id.detail_image);
        item_name = v.findViewById(R.id.item_name);
        start_date = v.findViewById(R.id.start_date);
        item_description = v.findViewById(R.id.item_description);
        end_date = v.findViewById(R.id.end_date);
        item_price = v.findViewById(R.id.item_price);
        item_owner = v.findViewById(R.id.item_owner);
        bid_now = v.findViewById(R.id.bid_now);
        bidders_list = v.findViewById(R.id.bidders_list);

        bid_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bidNow();
            }
        });

        Picasso.get().load(new File(singleDAO.getSingleInstance().getImg_path())).into(detail_image);
        item_name.setText(singleDAO.getSingleInstance().getItem_name());
        item_description.setText(singleDAO.getSingleInstance().getDescription());
        start_date.setText(singleDAO.getSingleInstance().getStart_date());
        end_date.setText(singleDAO.getSingleInstance().getEnd_date());

        item_price.setText(singleDAO.getSingleInstance().getPrice());
        item_owner.setText(singleDAO.getSingleInstance().getUser_name());
        //set all bid history to the history list

        adapter = new bid_history_adapter(db.getBiddingInfo(String.valueOf(singleDAO.getSingleInstance().getItem_id())));

        bidders_list.setLayoutManager(new LinearLayoutManager(getContext()));
        bidders_list.setAdapter(adapter);

    }

    private void bidNow() {

        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/AUCTION/file/state.csv");

            if (file.exists() || !singleDAO.getSingleInstance().getUserIdSession().equals("empty")) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new bid_now_buy()).addToBackStack("").commit();
            } else {

                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new login()).commit();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}