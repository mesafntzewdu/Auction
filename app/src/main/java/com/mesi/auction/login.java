package com.mesi.auction;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.Objects;

public class login extends Fragment {

    EditText user_name;
    EditText password;
    CheckBox save_next;
    DBHelper db;
    Button signin;
    TextView login_error;
    TextView no_account;
    boolean saveLogin = false;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        user_name = v.findViewById(R.id.user_name);
        password = v.findViewById(R.id.password);
        signin = v.findViewById(R.id.signin);
        save_next = v.findViewById(R.id.save_next);
        db = new DBHelper(getContext());
        login_error = v.findViewById(R.id.login_error);
        no_account = v.findViewById(R.id.no_account);


        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new register()).commit();
            }
        });

        saveLogin();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check and if the user is admin load him admin module
                checkIfLoginIsAdmin(user_name.getText().toString().trim(), password.getText().toString().trim());
            }
        });

        return v;
    }

    private void checkIfLoginIsAdmin(String user_name, String password) {

        if (user_name.equals("admin")&&password.equals("admin"))
        {
          Intent i = new Intent(getActivity(), AdminHome.class);
          startActivity(i);
        }else
        {
            checkUserLoginCr();
        }

    }

    //login saving information saved
    private void saveLogin() {

        save_next.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    
                    saveLogin = true;
                }

                if (!compoundButton.isChecked())
                {
                    saveLogin = false;
                }
            }
        });

    }

    public void writeSaveInfo() {
        File file = new File(Environment.getExternalStorageDirectory() + "/AUCTION/file/state.csv");

        try {

            DBHelper db = new DBHelper(getContext());

            FileWriter fw = new FileWriter(file);

            CSVWriter csvWrite = new CSVWriter(fw);

            String[] userListArray = {db.getUserId(user_name.getText().toString())};

            csvWrite.writeNext(userListArray);
            csvWrite.flush();
            csvWrite.close();
            fw.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkUserLoginCr() {

        if (db.checkUserIfExists(user_name.getText().toString().trim()))
        {

            singleDAO.getSingleInstance().setUserIdSession(db.getUserId(user_name.getText().toString()));

           if (saveLogin)
           {
               File fileTest = new File(Environment.getExternalStorageDirectory() + "/AUCTION/file/");
               if (fileTest.exists()) {
                   writeSaveInfo();
               } else {
                   fileTest.mkdirs();
                   writeSaveInfo();

               }
           }

            Intent i = new Intent(getActivity(), memberActivity.class);
             requireActivity().finish();
            startActivity(i);

        }else
        {

            Toast.makeText(getContext(), "Invalid credential", Toast.LENGTH_SHORT).show();

            getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new login()).commit();

        }
    }
}