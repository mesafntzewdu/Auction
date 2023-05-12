package com.mesi.auction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mesi.auction.dao.DAO;
import com.mesi.auction.dao.bought_item_single_dao;
import com.mesi.auction.dao.userDAO;
import com.mesi.auction.database.DBHelper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class bought_item_detail extends Fragment {

    ImageView bought_item_img;
    TextView bought_item_name;
    TextView bought_item_price;
    TextView bought_item_description;
    TextView bought_item_soldby;
    ImageView sold_by_description;
    TextView confirmation_number;
    EditText bought_item_conf;
    Button bought_item_confirm_button;

    DBHelper db;


    public bought_item_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bought_item_detail, container, false);

        bought_item_img = v.findViewById(R.id.bought_item_img);
        bought_item_name = v.findViewById(R.id.bought_item_name);
        bought_item_price = v.findViewById(R.id.bought_item_price);
        bought_item_description = v.findViewById(R.id.bought_item_description);
        bought_item_soldby = v.findViewById(R.id.bought_item_soldby);
        sold_by_description = v.findViewById(R.id.sold_by_description);
        confirmation_number = v.findViewById(R.id.confirmation_number);
        bought_item_conf = v.findViewById(R.id.bought_item_conf);
        bought_item_confirm_button = v.findViewById(R.id.bought_item_confirm_button);

        db = new DBHelper(getContext());


        if (db.userIsPaid(bought_item_single_dao.getBoughtInstance().getItem_id()))
        {
            Log.d("dfjdfdfdfdf", ""+db.userIsPaid(bought_item_single_dao.getBoughtInstance().getItem_id()));
            bought_item_confirm_button.setEnabled(false);
            Log.d("button disabled", "disabled");
        }

        try {
            Picasso.get().load(new File(bought_item_single_dao.getBoughtInstance().getImgPath())).into(bought_item_img);
        } catch (Exception e) {

            e.printStackTrace();
        }


        bought_item_name.setText(bought_item_single_dao.getBoughtInstance().getItemName());
        bought_item_price.setText(bought_item_single_dao.getBoughtInstance().getHigherPrice());
        bought_item_description.setText(bought_item_single_dao.getBoughtInstance().getDescription());
        bought_item_soldby.setText(bought_item_single_dao.getBoughtInstance().getSoldBy());
        confirmation_number.setText(bought_item_single_dao.getBoughtInstance().getConfNumber());


        sold_by_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soldByDescription();
            }
        });

        bought_item_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForConfirmationAndTransferMoney();
            }
        });


        return v;
    }

    //CHECK USER ITEM CONFIRMATION NUMBER AND TRANSFER MONEY TO THE SELLER
    private void checkForConfirmationAndTransferMoney() {

        if (!bought_item_conf.getText().toString().trim().equals(bought_item_single_dao.getBoughtInstance().getConfNumber())) {
            Toast.makeText(getContext(), "Confirmation number does not match.", Toast.LENGTH_SHORT).show();

            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Notice:");
        alert.setMessage("Are you sure you wanna to transfer your money to the item seller?\n THE ACTION IS UNREVERSIBLE");

        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               if ( db.transferBuyerMoney(bought_item_single_dao.getBoughtInstance().getUserId(), bought_item_single_dao.getBoughtInstance().getItem_id()))
               {
                   Toast.makeText(getContext(), "Successfully Paid.", Toast.LENGTH_SHORT).show();
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new bought_items()).commit();
               }

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

    private void soldByDescription() {

        ArrayList<userDAO> userDetail;

        userDetail = db.getSellerDetail(bought_item_single_dao.getBoughtInstance().getUserId());

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Seller information");

        View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);

        ImageView alertImage = dialogView.findViewById(R.id.alertImage);
        TextView alert_sell_name = dialogView.findViewById(R.id.alert_sell_name);
        TextView alert_father_name = dialogView.findViewById(R.id.alert_father_name);
        TextView alert_email = dialogView.findViewById(R.id.alert_email);
        TextView alert_phone = dialogView.findViewById(R.id.alert_phone);

        try {
            Picasso.get().load(new File(userDetail.get(0).getImgPath())).into(alertImage);
        } catch (Exception e) {

        }

        alert_sell_name.setText(userDetail.get(0).getUser_name());
        alert_father_name.setText(userDetail.get(0).getFather_name());
        alert_email.setText(userDetail.get(0).getEmail());
        alert_phone.setText(userDetail.get(0).getPhone());

        alert.setView(dialogView);

        AlertDialog builder = alert.create();
        builder.show();

    }
}