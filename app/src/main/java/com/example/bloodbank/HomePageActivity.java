package com.example.bloodbank;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
DrawerLayout drawerLayout;
NavigationView navigationView;
ActionBarDrawerToggle actionBarDrawerToggle;
Toolbar toolbar;
FragmentManager fragmentManager;
FragmentTransaction fragmentTransaction;
boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        navigationView =findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout=findViewById(R.id.drawer);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new MainFragment());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        if(item.getItemId()==R.id.nearbydon){

            fragmentTransaction.replace(R.id.container_fragment,new MainFragment());
            fragmentTransaction.commit();
        }else if(item.getItemId()==R.id.requestdon){
            fragmentTransaction.replace(R.id.container_fragment,new Request_fragment());
            fragmentTransaction.commit();
        }else if(item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();

            System.exit(1);

        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
