package com.mesi.auction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mesi.auction.database.DBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class register extends Fragment {

    private String[] PERMISSION_ARRAY;

    Bitmap bml = null;
    ImageView user_image;
    EditText user_name;
    EditText father_name;
    EditText email;
    EditText phone;
    EditText password;
    EditText re_password;
    RadioButton male;
    RadioButton female;
    TextView already_account;
    ImageView go_back;
    TextView login_error;

    private String imageAbsolutePath;

    DBHelper db;
    private String sex;
    Button register_button;

    public register() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        PERMISSION_ARRAY = new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        login_error = v.findViewById(R.id.login_error);

        widgetMethod(v);

        return v;
    }

    private void widgetMethod(View v) {

        user_image = v.findViewById(R.id.user_image);
        user_name = v.findViewById(R.id.user_name);
        father_name = v.findViewById(R.id.father_name);
        email = v.findViewById(R.id.email);
        phone = v.findViewById(R.id.phone);
        password = v.findViewById(R.id.password);
        re_password = v.findViewById(R.id.re_password);
        male = v.findViewById(R.id.male);
        female = v.findViewById(R.id.female);
        already_account = v.findViewById(R.id.already_account);
        go_back = v.findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), visitorsActivity.class);
                startActivity(i);
            }
        });

        already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new login()).commit();
            }
        });
        male.setChecked(true);

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sex = "male";
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                sex = "female";
            }
        });


        register_button = v.findViewById(R.id.register_button);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

    }

    private void registerNewUser() {

        if (user_name.getText().toString().equals("")) {
            Toast.makeText(getContext(), "User name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (father_name.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Father name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Email should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Phone should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.PHONE.matcher(phone.getText().toString()).matches())
        {
            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Password should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.getText().toString().equals(re_password.getText().toString())) {
            Toast.makeText(getContext(), "Password should be the same", Toast.LENGTH_SHORT).show();
            return;
        }

        String regex = "^(?=.*)[A-Za-z\\d@$!%*?&]{8,}$";


        if (!password.getText().toString().matches(regex))
        {
            login_error.setText("Your password should be 8 digits and should not have space.");
            return ;
        }

            db = new DBHelper(getContext());

        if (db.insertUser(user_name.getText().toString(), father_name.getText().toString(), email.getText().toString(), phone.getText().toString(), sex, password.getText().toString(), imageAbsolutePath)) {
            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new login()).commit();
        } else {
            Toast.makeText(getContext(), "Not successful", Toast.LENGTH_SHORT).show();
        }
    }
}

