package com.mesi.auction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class report extends Fragment {

    MultiAutoCompleteTextView item_report;
    Button submit_report;

    public report() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_report, container, false);

        item_report = v.findViewById(R.id.item_report);
        submit_report = v.findViewById(R.id.submit_report);
        reportSubmit();
        return v;
    }

    public void reportSubmit()
    {
        submit_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (item_report.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getContext(), "Report should not be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHelper db = new DBHelper(getContext());

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

                if (db.insertReportOnce(user_id[0], singleDAO.getSingleInstance().getItem_id(), item_report.getText().toString()))
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new home()).commit();
                }else
                {
                    Toast.makeText(getContext(), "You already reported this item.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}