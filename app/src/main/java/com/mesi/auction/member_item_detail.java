package com.mesi.auction;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.database.DBHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class member_item_detail extends Fragment {
    ImageView member_item_image;
    EditText member_item_name;
    EditText member_item_price;
    MultiAutoCompleteTextView member_item_description;
    private String[] PERMISSION_ARRAY;
    EditText member_item_category;
    TextView member_item_enddate;
    Button member_item_update;
    Button member_item_delete;

    String updateImagePath;
    Spinner category;

    private String categorySelected;

    private Bitmap bml;
    DBHelper db;


    public member_item_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_member_item_detail, container, false);
        member_item_image = v.findViewById(R.id.member_item_image);
        member_item_name = v.findViewById(R.id.member_item_name);
        member_item_price = v.findViewById(R.id.member_item_price);
        member_item_description = v.findViewById(R.id.member_item_description);
        member_item_category = v.findViewById(R.id.member_item_category);
        member_item_enddate = v.findViewById(R.id.member_item_enddate);

        PERMISSION_ARRAY = new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        db = new DBHelper(getContext());

        member_item_update = v.findViewById(R.id.member_item_update);
        member_item_delete = v.findViewById(R.id.member_item_delete);

        Picasso.get().load(new File(singleDAO.getSingleInstance().getImg_path())).into(member_item_image);

        updateImagePath = singleDAO.getSingleInstance().getImg_path();

        member_item_name.setText(singleDAO.getSingleInstance().getItem_name());
        member_item_price.setText(singleDAO.getSingleInstance().getPrice());
        member_item_description.setText(singleDAO.getSingleInstance().getDescription());
        member_item_category.setText(singleDAO.getSingleInstance().getCategory());
        member_item_enddate.setText(singleDAO.getSingleInstance().getEnd_date());

        member_item_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(singleDAO.getSingleInstance().getItem_id());
            }
        });

        member_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(singleDAO.getSingleInstance().getItem_id());
            }
        });

        spinnerListener(v);
        getImageAndSave();

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

    private void getImageAndSave() {
        member_item_image.setOnClickListener(view ->
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
                    member_item_image.setImageBitmap(bm);
                }
            }

            //image from gallery
            if (requestCode == 121) {

                if (data != null) {

                    try {

                        InputStream input = getContext().getContentResolver().openInputStream(data.getData());
                        Bitmap bm = BitmapFactory.decodeStream(input);
                        bml = bm;

                        member_item_image.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

        }
    }


    private void deleteItem(int item_id) {

       AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
       alert.setTitle("Alert!");
       alert.setMessage("Do you really wanna to delete");
       alert.setCancelable(true);
       alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

               dialogInterface.dismiss();
           }
       });

       alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               if (db.deleteItem(item_id)) {
                   new File(singleDAO.getSingleInstance().getImg_path()).delete();
                   Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();

                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new my_items()).commit();
               }else
               {
                   Toast.makeText(getContext(), "You can't delete item while it is being active!", Toast.LENGTH_SHORT).show();
               }
           }
       });

        AlertDialog builder = alert.create();
        builder.show();


    }


    private void updateItem(int item_id) {

        if (!checkStoragePermissionIfGranted())
        {
            storageLauncher.launch(PERMISSION_ARRAY[1]);
        }


        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Alert!");
        alert.setMessage("Do you really wanna to update");
        alert.setCancelable(true);
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //SAVE IMAGE ABSOLUTE PATH IN THE DATABASE HERE WHEN THE USER CLICK THE REGISTER BUTTON
                getBitmapAndStoreItOnTheObjct();

                if (db.updateItem(item_id, member_item_name.getText().toString(), member_item_price.getText().toString(),
                        member_item_description.getText().toString(), categorySelected,
                        member_item_enddate.getText().toString(), updateImagePath)) {
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new my_items()).commit();

                }else
                {
                    Toast.makeText(getContext(), "You can't update item while it is being active!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog builder = alert.create();
        builder.show();
    }
    private void getBitmapAndStoreItOnTheObjct() {

        if (checkStoragePermissionIfGranted()) {
            //Then save image inside the folder in this method
            saveImageInsideTheFolder();

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
                updateImagePath = file.getAbsolutePath();

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