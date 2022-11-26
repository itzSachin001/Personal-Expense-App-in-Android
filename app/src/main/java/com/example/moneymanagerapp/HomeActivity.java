package com.example.moneymanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar myToolBar;
    BottomNavigationView bottomNavigation;
    FrameLayout mainFrame;

    Fragment DashboardFragment,IncomeFragment,ExpensesFragment,SignOutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        myToolBar=findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);



        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Expense Manager");
        }

        drawerLayout=findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,myToolBar,R.string.open_drawer,R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView=findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        bottomNavigation=findViewById(R.id.bottomNavigation);
        mainFrame=findViewById(R.id.mainFrame);

        DashboardFragment=new DashboardFragment();
        IncomeFragment=new IncomeFragment();
        ExpensesFragment=new ExpensesFragment();
        SignOutFragment=new SignOutFragment();

        setFragment(DashboardFragment);

        bottomNavigation.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.dashboard:
                    setFragment(DashboardFragment);
                    return true;

                case R.id.income:
                    setFragment(IncomeFragment);
                    return true;

                case R.id.expenses:
                    setFragment(ExpensesFragment);
                    return true;

                case R.id.signOut:
                    setFragment(SignOutFragment);
                    return true;
            }
            return true;
        });
    }


    public void setFragment(Fragment fragment){
        FragmentTransaction fm=getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.mainFrame,fragment);
        fm.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void displaySelectedListener(int itemId){

        Fragment fragment=null;

        switch(itemId){

            case R.id.dashboard:
                fragment=new DashboardFragment();
                break;

            case R.id.income:
                fragment=new IncomeFragment();
                break;

            case R.id.expenses:
                fragment=new ExpensesFragment();
                break;

            case R.id.signOut:
                fragment=new SignOutFragment();
                break;
        }

        if(fragment!=null){

            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,fragment);
            ft.commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        displaySelectedListener(item.getItemId());

        return true;
    }
}