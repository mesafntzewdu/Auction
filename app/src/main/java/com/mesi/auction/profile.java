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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.dao.userDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class profile extends Fragment {

    EditText user_name;
    EditText father_name;
    EditText old_password;
    EditText new_password;
    TextView balance;
    Button update_uname;
    Button update_fname;
    Button update_password;
    Button update_prof_img;
    ImageView profile_img;
    ImageView acc_info;

    DBHelper db;

    private String imageAbsolutePath = "";
    private String[] PERMISSION_ARRAY;
    Bitmap bml = null;

    ArrayList<userDAO> userDetail;

    String[] user_id;

    public profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new DBHelper(getContext());
        PERMISSION_ARRAY = new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        user_id = new String[2];

        getUserProfile();
        initWidget(v);
        getBalance();
        getImageAndSave(v);

        return v;
    }

    public void getUserProfile() {

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

        userDetail = db.getSellerDetail(Integer.parseInt(user_id[0]));

    }

    private void getImageAndSave(View v) {

        profile_img = v.findViewById(R.id.profile_img);

        try {
            Picasso.get().load(new File(userDetail.get(0).getImgPath())).into(profile_img);
        } catch (Exception e) {
            if (imageAbsolutePath.isEmpty()) {
                profile_img.setImageResource(R.drawable.clickme);

            }
        }

        profile_img.setOnClickListener(view ->
        {
            if (checkCameraPermissionIfGranted())
                takePicCameraGallery();
            else
                cameraLauncher.launch(PERMISSION_ARRAY[0]);
        });

    }

    ActivityResultLauncher<String> storageLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            getBitmapAndStoreItOnTheObjct();
        } else {
            Toast.makeText(getContext(), "To continue \"Storage\" permission has to be allowed.", Toast.LENGTH_SHORT).show();
        }
    });

    //Camera permission here
    ActivityResultLauncher<String> cameraLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            takePicCameraGallery();
        } else {
            Toast.makeText(getContext(), "To continue \"Camera\" permission has to be allowed.", Toast.LENGTH_SHORT).show();
        }

    });

    //check camera permission and if it is granted allow the user to continue to the next step
    public boolean checkCameraPermissionIfGranted() {
        return ContextCompat.checkSelfPermission(getContext(), PERMISSION_ARRAY[0]) == PackageManager.PERMISSION_GRANTED;
    }

    private void takePicCameraGallery() {

        String[] values = {"Camera", "Gallery"};
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setCancelable(true);
        alert.setItems(values, (dialogInterface, itemPosition) ->
        {
            if (values[itemPosition].equals("Camera"))
                getImageFormCamera();
            if (values[itemPosition].equals("Gallery"))
                getImageFromGallery();
        });
        AlertDialog builder = alert.create();
        builder.show();
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 121);
    }

    private void getImageFormCamera() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 120);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //image from camera
            if (requestCode == 120) {

                if (data != null) {
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    bml = bm;
                    profile_img.setImageBitmap(bm);
                }
            }

            //image from gallery
            if (requestCode == 121) {

                if (data != null) {

                    try {

                        InputStream input = getContext().getContentResolver().openInputStream(data.getData());
                        Bitmap bm = BitmapFactory.decodeStream(input);
                        bml = bm;

                        profile_img.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

        }
    }


    private void getBitmapAndStoreItOnTheObjct() {

        if (checkStoragePermissionIfGranted()) {
            //Then save image inside the folder in this method
            saveImageInsideTheFolder();
            Log.d("Absolute path", imageAbsolutePath);

        } else {
            requestPermissions(PERMISSION_ARRAY, 201);
        }
    }

    //Save image inside the folder
    private void saveImageInsideTheFolder() {
        try {


            if (createFolder()) {

                Calendar cal = Calendar.getInstance();
                File file = new File(Environment.getExternalStorageDirectory() + "/Gym_Data/file/", "gym_img" + cal.getTimeInMillis() + ".png");
                FileOutputStream fout = new FileOutputStream(file);

                bml.compress(Bitmap.CompressFormat.PNG, 85, fout);

                //save image absolute path in the local variable to store on the database
                imageAbsolutePath = file.getAbsolutePath();

                fout.flush();
                fout.close();

                Log.d("BitMap", "BitMap successfully created");
            }

        } catch (Exception e) {

        }
    }

    //Create folder inside the external storage
    public boolean createFolder() {
        File f = new File(Environment.getExternalStorageDirectory(), "/Gym_Data/file");
        if (f.exists())
            return true;

        return f.mkdirs();
    }

    //check if permission is granted
    private boolean checkStoragePermissionIfGranted() {
        return ContextCompat.checkSelfPermission(getContext(), PERMISSION_ARRAY[1]) == PackageManager.PERMISSION_GRANTED;
    }


    private void initWidget(View v) {

        user_name = v.findViewById(R.id.user_name);
        father_name = v.findViewById(R.id.father_name);
        old_password = v.findViewById(R.id.old_password);
        new_password = v.findViewById(R.id.new_password);
        balance = v.findViewById(R.id.balance);

        update_uname = v.findViewById(R.id.update_uname);
        update_fname = v.findViewById(R.id.update_fname);
        update_password = v.findViewById(R.id.update_password);
        update_prof_img = v.findViewById(R.id.update_prof_img);

        acc_info = v.findViewById(R.id.acc_info);

        acc_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAccountInformation();
            }
        });

        update_prof_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileImage();
            }
        });

        update_uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserName();
            }
        });

        update_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFname();
            }
        });

        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

    }

    private void showAccountInformation() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Account information");

        View dialogView = getLayoutInflater().inflate(R.layout.fragment_account_information, null);

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

        Button acc_submit = dialogView.findViewById(R.id.acc_submit);
        EditText get_acc_no = dialogView.findViewById(R.id.get_acc_no);

        String[] finalUser_id = user_id;
        acc_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertUserAccount(get_acc_no, finalUser_id[0]);
            }
        });


        alert.setView(dialogView);

        AlertDialog builder = alert.create();
        builder.show();

    }

    private void insertUserAccount(EditText get_acc_no, String u_id) {

        if (get_acc_no.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Account number can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        DBHelper db = new DBHelper(getContext());

        if (db.insertUserAccNo(u_id, get_acc_no.getText().toString())) {
            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "You already added one!", Toast.LENGTH_SHORT).show();
        }
    }

    //Update profile image
    private void updateProfileImage() {
        if (checkStoragePermissionIfGranted()) {

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


            //SAVE IMAGE ABSOLUTE PATH IN THE DATABASE HERE WHEN THE USER CLICK THE REGISTER BUTTON
            getBitmapAndStoreItOnTheObjct();

            if (imageAbsolutePath.isEmpty()) {
                Toast.makeText(getContext(), "You have to select image first by clicking your profile.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.updateUserProfile(user_id[0], imageAbsolutePath)) {
                Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Not updated try again", Toast.LENGTH_SHORT).show();
            }

        } else {
            storageLauncher.launch(PERMISSION_ARRAY[1]);
        }
    }


    public void getBalance() {

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

        balance.setText(String.valueOf(db.getUserBalance(user_id[0])));
    }

    private void updateFname() {

        if (father_name.getText().toString().length() <= 2) {
            Toast.makeText(getContext(), "Father name must be more than 3 character!", Toast.LENGTH_SHORT).show();

            return;
        }

        if (db.updateFname(father_name.getText().toString(), user_id[0])) {
            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Not successful", Toast.LENGTH_SHORT).show();
        }

    }

    private void updatePassword() {


        if (db.checkOldPassword(old_password.getText().toString(), user_id[0])) {
            Toast.makeText(getContext(), "Old password don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (new_password.getText().toString().length() <= 7) {
            Toast.makeText(getContext(), "Password length must be more than 8 digits", Toast.LENGTH_SHORT).show();

            return;
        }

        if (db.updatePassword(new_password.getText().toString(), user_id[0])) {
            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Not successful", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserName() {

        if (user_name.getText().toString().length() < 2) {
            Toast.makeText(getContext(), "User name must be more than 3 character!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.updateUserName(user_name.getText().toString(), user_id[0])) {
            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Not successful", Toast.LENGTH_SHORT).show();
        }

    }
}