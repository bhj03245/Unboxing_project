package com.example.drawerlayouttest;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Menu {


    public Menu(){}


//    public void InitializeLayout(Toolbar toolbar, final DrawerLayout drawerLayout, NavigationView navigationView,
//                                  final Context context){
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId())
//                {
//                    case R.id.item1:
//                        Intent in = new Intent(context, UserSetting.class);
//                        context.startActivity(in);
//
//
//
//                    case R.id.item2:
//                        Toast.makeText(context, "SelectedItem 2", Toast.LENGTH_SHORT).show();
//                    case R.id.item3:
//                        Toast.makeText(context, "SelectedItem 3", Toast.LENGTH_SHORT).show();
//                    case R.id.norm_sel:
//
//                    case R.id.manl_sel:
//
//                    case R.id.park_sel:
//
//                    case R.id.impt_sel:
//                }
//
//
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });
//    }
}
