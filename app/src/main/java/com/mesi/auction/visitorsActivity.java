package com.mesi.auction;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.mesi.auction.adapter.ImageAdapter;
import com.mesi.auction.dao.singleDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;

public class visitorsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AUCTION ERA");
        setSupportActionBar(toolbar);

        File file = new File(Environment.getExternalStorageDirectory() + "/AUCTION/file/state.csv");
        if (file.exists())
        {
            Intent i = new Intent(this, memberActivity.class);
            //this.finish();
            startActivity(i);
        }

        //check if the user is on the set mode
        bidNow();
        //Home category click listner
        categoryListener();

        //Image adapter and slider
        adapterAndSlider();

        if (!checkPermissionGran())
        {
            checkStoragePermission();
        }
    }

    private boolean checkPermissionGran() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            return Environment.isExternalStorageManager();
        }else{

            return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void checkStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
           try {
               if (!Environment.isExternalStorageManager())
               {
                   Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                   Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                   i.setData(uri);
                   startActivity(i);
               }
           }catch (Exception e)
           {
               e.printStackTrace();
               Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
               startActivity(i);

           }

        }else
        {
            storagePermissoinAPI29.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    ActivityResultLauncher<String> storagePermissoinAPI29 = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted->{


    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visitor_menu, menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.amharic_id:
                        menuItem.setChecked(true);
                        break;

                    case R.id.english_id:
                        menuItem.setChecked(true);
                        break;
                }

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void bidNow() {


        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/AUCTION/file/state.csv");

            if (file.exists() || !singleDAO.getSingleInstance().getUser_session().equals("empty")) {
                Intent i = new Intent(this, memberActivity.class);
                startActivity(i);
            }
        } catch (Exception e) {

        }
    }

    private void adapterAndSlider() {
        ViewPager2 viewPager2 = findViewById(R.id.slide_image);

        List<Integer> resourceList = new ArrayList<>();
        resourceList.add(R.drawable.moto);

        ImageAdapter adapter = new ImageAdapter(resourceList);

        viewPager2.setAdapter(adapter);
    }

    private void categoryListener() {

        ImageView cars = findViewById(R.id.cars);
        ImageView jewelry = findViewById(R.id.jewelry);
        ImageView home = findViewById(R.id.home);
        ImageView antiques = findViewById(R.id.antiques);
        ImageView spare = findViewById(R.id.spare);
        ImageView furniture = findViewById(R.id.furniture);
        ImageView memorable = findViewById(R.id.memorable);
        ImageView art = findViewById(R.id.art);
        ImageView watch = findViewById(R.id.watch);
        ImageView book = findViewById(R.id.book);
        ImageView appliance = findViewById(R.id.appliance);
        ImageView others = findViewById(R.id.others);

        Button subscribe = findViewById(R.id.subscribe_button);

        //Listener method for each category
        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                singleDAO.getSingleInstance().setGeneralCat("Vehicle");
                fragmentSwitch(new List_items());

            }
        });

        jewelry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Jewelry");
                fragmentSwitch(new List_items());
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Home");
                fragmentSwitch(new List_items());
            }
        });

        antiques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Antiquities");
                fragmentSwitch(new List_items());
            }
        });

        spare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Spares");
                fragmentSwitch(new List_items());
            }
        });

        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Furniture");
                fragmentSwitch(new List_items());
            }
        });

        memorable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Memorable");
                fragmentSwitch(new List_items());
            }
        });

        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Art");
                fragmentSwitch(new List_items());
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Watches");
                fragmentSwitch(new List_items());
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Books");
                fragmentSwitch(new List_items());
            }
        });

        appliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Electronics");
                fragmentSwitch(new List_items());
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleDAO.getSingleInstance().setGeneralCat("Other");
                fragmentSwitch(new List_items());
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(visitorsActivity.this, "Subscribe is clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fragmentSwitch(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();

    }
}