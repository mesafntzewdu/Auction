package com.mesi.auction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class bid_now_buy extends Fragment {

    ImageView single_image;
    TextView item_name;
    TextView item_price;
    TextView day_left;
    TextView description;
    Button buy_item;
    TextView report;
    EditText bid_price;
    TextView bid_price_error;

    DBHelper db;

    public bid_now_buy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bid_now_buy, container, false);

        single_image = v.findViewById(R.id.single_image);
        item_name = v.findViewById(R.id.single_item_name);
        item_price = v.findViewById(R.id.single_item_price);
        day_left = v.findViewById(R.id.single_dayleft);
        description = v.findViewById(R.id.single_description);
        buy_item = v.findViewById(R.id.buy_item);
        report = v.findViewById(R.id.report);
        bid_price = v.findViewById(R.id.bid_price);
        bid_price_error = v.findViewById(R.id.bid_price_error);

        db = new DBHelper(getContext());

        if (db.checkIfItemIsSold(singleDAO.getSingleInstance().getItem_id()))
        {
            buy_item.setEnabled(false);
        }



        userReport();

        Picasso.get().load(new File(singleDAO.getSingleInstance().getImg_path())).into(single_image);
        item_name.setText(singleDAO.getSingleInstance().getItem_name());
        item_price.setText(singleDAO.getSingleInstance().getPrice());
        day_left.setText(singleDAO.getSingleInstance().getEnd_date());
        description.setText(singleDAO.getSingleInstance().getDescription());

        buy_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bidNowItem();
            }
        });

        return v;
    }

    private void bidNowItem() {

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

        if (bid_price.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Bid price should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Float.parseFloat(bid_price.getText().toString()) <= Float.parseFloat(singleDAO.getSingleInstance().getPrice())) {
            Toast.makeText(getContext(), "Your price should be higher that the start value!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Float.parseFloat(bid_price.getText().toString()) <= db.getHignerPriceFromBidHistory(String.valueOf(singleDAO.getSingleInstance().getItem_id()))) {
            Toast.makeText(getContext(), "You should bid with higher price than the current value!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (db.getUserBalance(user_id[0]) < Float.parseFloat(bid_price.getText().toString())) {
            Toast.makeText(getContext(), "Insufficient balance!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (singleDAO.getSingleInstance().getUser_id_db().equals(user_id[0])) {
            Toast.makeText(getContext(), "Sorry you can't bid on your item!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Calculate and subtract the user balance form the acc and if the higher price comes from other user return the amount for the last user

        //add the bidders information in to the bid history table

        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat smp = new SimpleDateFormat("dd/MM/yyyy");

        updateUserBalance(String.valueOf(singleDAO.getSingleInstance().getItem_id()), user_id[0], Float.parseFloat(bid_price.getText().toString()));

        if (db.insertBuyerInfo(user_id[0], String.valueOf(singleDAO.getSingleInstance().getItem_id()), bid_price.getText().toString(), smp.format(cal.getTime()))) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new home()).commit();
        } else {
            Toast.makeText(getContext(), "Not successful please try again later.", Toast.LENGTH_SHORT).show();

        }
    }

    private void updateUserBalance(String itemId, String user_id, Float bid_price) {

        db.updateUserBalance(itemId, user_id, bid_price);

    }

    public void userReport()
    {
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new report()).commit();
            }
        });
    }

}