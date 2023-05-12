package com.mesi.auction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminHome extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        toolbar = findViewById(R.id.toolbarad);
        switchToFragment(new admin_all_item());
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.all_item:
                        switchToFragment(new admin_all_item());
                        break;
                    case R.id.approval:
                        switchToFragment(new admin_approval());
                        break;
                    case R.id.report:

                        break;
                    case R.id.pending:

                        break;
                }
                return true;
            }
        });

        toolbar.setTitle("BID ERA ADMIN");
        setSupportActionBar(toolbar);
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new login()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.amdin_tlmenu, menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.logout:
                        deleteAndChangeSession();
                        break;
                }

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void deleteAndChangeSession() {

        Intent i = new Intent(this, visitorsActivity.class);
        startActivity(i);
        this.finish();
    }
}