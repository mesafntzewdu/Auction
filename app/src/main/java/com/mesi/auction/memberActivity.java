package com.mesi.auction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mesi.auction.dao.singleDAO;

import java.io.File;
import java.util.List;

public class memberActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BID ERA");
        setSupportActionBar(toolbar);

        //first lead home method
        navigationMethod();
        fragmentSwitch(new home());
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new login()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_menu, menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.amharic_id:

                    case R.id.english_id:
                        menuItem.setChecked(true);
                        break;

                    case R.id.logout_sh:
                        deleteAndChangeSession();
                        break;

                    case R.id.profile_sh:
                        switchToProfile();
                        break;
                }

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void switchToProfile() {

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile()).commit();

    }

    private void deleteAndChangeSession() {
        new File(Environment.getExternalStorageDirectory()+"/AUCTION/file/state.csv").delete();
        singleDAO.getSingleInstance().setUserIdSession("empty");
        Intent i = new Intent(this, visitorsActivity.class);
        this.finish();
        startActivity(i);
    }

    private void navigationMethod() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        fragmentSwitch(new home());
                        break;
                    case R.id.sell_item:
                        fragmentSwitch(new sell_item());
                        break;
                    case R.id.my_items:
                        fragmentSwitch(new my_items());
                        break;
                    case R.id.bought_item:
                        fragmentSwitch(new bought_items());
                        break;
                }

                return true;
            }
        });
    }

    private void fragmentSwitch(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


}