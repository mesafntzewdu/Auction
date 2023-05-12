package com.mesi.auction;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mesi.auction.adapter.bid_history_adapter;
import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

public class admin_approval_detail extends Fragment {

    ImageView detail_image;
    TextView item_name;
    TextView item_description;
    TextView end_date;
    TextView start_date;
    TextView item_price;
    TextView item_owner;

    RecyclerView bidders_list;
    bid_history_adapter adapter;

    DBHelper db;
    Button admin_approve_item;


    public admin_approval_detail() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_admin_approval_detail, container, false);

        db = new DBHelper(getContext());

        setZdiscription(v);

        return v;
    }


    private void setZdiscription(View v) {

        detail_image = v.findViewById(R.id.detail_image);
        item_name = v.findViewById(R.id.item_name);
        start_date = v.findViewById(R.id.start_date);
        item_description = v.findViewById(R.id.item_description);
        end_date = v.findViewById(R.id.end_date);
        item_price = v.findViewById(R.id.item_price);
        item_owner = v.findViewById(R.id.item_owner);
        admin_approve_item = v.findViewById(R.id.admin_approve_item);
        bidders_list = v.findViewById(R.id.bidders_list);

        admin_approve_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItemApprove();
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

    private void updateItemApprove() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Alert.");
        alert.setMessage("Do you really wanna to update this item.");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (db.approveItem(singleDAO.getSingleInstance().getItem_id())) {
                    Toast.makeText(getContext(), "Successfully updated.", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new admin_approval()).commit();
                } else
                    Toast.makeText(getContext(), "Not successful.", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();

    }

}