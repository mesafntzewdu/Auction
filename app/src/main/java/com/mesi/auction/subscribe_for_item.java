package com.mesi.auction;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
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


public class subscribe_for_item extends Fragment {

  ImageView backImage;
  Button subscribe_for_item;
  TextView email_error;
  EditText sub_visitor_email;
  DBHelper db;

    public subscribe_for_item() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subscribe_for_item, container, false);

        backImage = v.findViewById(R.id.go_back);
        email_error = v.findViewById(R.id.email_error);
        sub_visitor_email = v.findViewById(R.id.sub_visitor_email);
        email_error.setText("");

        db = new DBHelper(getContext());

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new no_item_notify()).commit();
            }
        });

        subscribe_for_item = v.findViewById(R.id.subscribe_for_item);
        subscribe_for_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailSubForVisitor();
            }
        });

        return v;
    }

    private void emailSubForVisitor() {

        if (sub_visitor_email.getText().toString().equals(""))
            return;
        if (Patterns.EMAIL_ADDRESS.matcher(sub_visitor_email.getText().toString()).matches())
        {


            if (db.addSubVisitor(sub_visitor_email.getText().toString(), singleDAO.getSingleInstance().getGeneralCat()))
            {
                email_error.setText("Successful");

                Intent i = new Intent(getActivity(), visitorsActivity.class);
                startActivity(i);
            }

        }else
        {
            email_error.setText("Invalid email address");
        }
    }
}