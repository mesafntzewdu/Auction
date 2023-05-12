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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class sell_item extends Fragment {

    Bitmap bml = null;
    private String categorySelected;
    private String imageAbsolutePath = "";

    private String[] PERMISSION_ARRAY;

    ImageView item_image;
    TextView item_name;
    TextView item_price;
    TextView item_description;

    TextView item_end_date;
    Spinner category;
    Button submit;

    DBHelper db;

    public sell_item() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item, container, false);
        PERMISSION_ARRAY = new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


        spinnerListener(v);
        getImageAndSave(v);
        buttonClickListner(v);
        return v;
    }


    private void spinnerListener(View v) {
        category = v.findViewById(R.id.category);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getImageAndSave(View v) {

        item_image = v.findViewById(R.id.item_image);
        item_image.setOnClickListener(view ->
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
                    item_image.setImageBitmap(bm);
                }
            }

            //image from gallery
            if (requestCode == 121) {

                if (data != null) {

                    try {

                        InputStream input = getContext().getContentResolver().openInputStream(data.getData());
                        Bitmap bm = BitmapFactory.decodeStream(input);
                        bml = bm;

                        item_image.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

        }
    }

    //Insert data into the database
    private void buttonClickListner(View v) {
        submit = v.findViewById(R.id.subscribe_button);

        submit.setOnClickListener(view -> {

            saveUserDataToDatabase(v);

        });
    }

    //Save user data into the database on user register method
    private void saveUserDataToDatabase(View v) {

        item_name = v.findViewById(R.id.item_name);
        item_price = v.findViewById(R.id.item_price);
        item_description = v.findViewById(R.id.item_description);
        item_end_date = v.findViewById(R.id.item_end_date);


        if (bml == null) {
            Toast.makeText(getContext(), "Image should not be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (item_name.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Name should not be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (item_price.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Price should not be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (item_description.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Description should not be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (item_end_date.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "End date should not be empty!", Toast.LENGTH_LONG).show();
            return;
        }

        if (Float.parseFloat(item_end_date.getText().toString())<1)
        {
            Toast.makeText(getContext(), "The minimum end date should be at list \"1\" ", Toast.LENGTH_LONG).show();
            return;
        }

        if (checkStoragePermissionIfGranted()) {
            //SAVE IMAGE ABSOLUTE PATH IN THE DATABASE HERE WHEN THE USER CLICK THE REGISTER BUTTON
            getBitmapAndStoreItOnTheObjct();

            db = new DBHelper(getContext());

            //Calculate item end date
            Calendar endDate = Calendar.getInstance();
            SimpleDateFormat endSdf = new SimpleDateFormat("dd/MM/yyyy");
            endDate.add(Calendar.DATE, Integer.parseInt(item_end_date.getText().toString()));

            //Item added current date
            Calendar startDate = Calendar.getInstance();
            SimpleDateFormat startSdf = new SimpleDateFormat("dd/MM/yyyy");

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


            if (db.insertItem(item_name.getText().toString(), item_price.getText().toString(), item_description.getText().toString(), categorySelected,startSdf.format(startDate.getTime()), endSdf.format(endDate.getTime()), imageAbsolutePath, String.valueOf(user_id[0])))
                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Not successful", Toast.LENGTH_SHORT).show();

        } else {
            storageLauncher.launch(PERMISSION_ARRAY[1]);
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
                File file = new File(Environment.getExternalStorageDirectory() + "/AUCTION/file/", "img" + cal.getTimeInMillis() + ".png");
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
        File f = new File(Environment.getExternalStorageDirectory(), "/AUCTION/file");
        if (f.exists())
            return true;

        return f.mkdirs();
    }

    //check if permission is granted
    private boolean checkStoragePermissionIfGranted() {
        return ContextCompat.checkSelfPermission(getContext(), PERMISSION_ARRAY[1]) == PackageManager.PERMISSION_GRANTED;
    }
}