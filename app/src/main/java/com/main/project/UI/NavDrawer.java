package com.main.project.UI;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.main.project.databinding.ActivityNavDrawerBinding;

public class NavDrawer extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavDrawerBinding binding;
    DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DataBaseHelper(this);

        binding = ActivityNavDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarNavDrawer.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        if(Login.getUserFromSharedPreferences(this).isAdmin()) {
            navigationView.getMenu().findItem(R.id.nav_dashboard_admin).setVisible(true);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home,
                    R.id.nav_dashboard_admin,
                    R.id.nav_car_menu,
                    R.id.nav_your_reservations,
                    R.id.nav_your_favorites,
                    R.id.nav_special_offers,
                    R.id.nav_profile,
                    R.id.nav_call_us,
                    R.id.nav_logout
            )
                    .setOpenableLayout(drawer)
                    .build();
        } else {
            navigationView.getMenu().findItem(R.id.nav_dashboard_admin).setVisible(false);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home,
                    R.id.nav_car_menu,
                    R.id.nav_your_reservations,
                    R.id.nav_your_favorites,
                    R.id.nav_special_offers,
                    R.id.nav_profile,
                    R.id.nav_call_us,
                    R.id.nav_logout
            )
                    .setOpenableLayout(drawer)
                    .build();
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        TextView navHeaderName = navigationView.getHeaderView(0).findViewById(R.id.User_name);

        User user = Login.getUserFromSharedPreferences(this);
        navHeaderName.setText(user.getFirstName() + " " + user.getLastName());



        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            performLogout();
            return true;
        });

    }

    private void performLogout() {
        User user = Login.getUserFromSharedPreferences(this);
        dbHelper.logoutUser(user.getEmail());

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}
