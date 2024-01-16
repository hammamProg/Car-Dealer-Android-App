package com.main.project.UI;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.main.project.databinding.ActivityNavDrawerBinding;

public class NavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavDrawerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavDrawer.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Set this class as the listener for navigation item clicks
        navigationView.setNavigationItemSelectedListener(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_car_menu,
                R.id.nav_your_reservations,
                R.id.nav_your_favorites,
                R.id.nav_special_offers
        )
                .setOpenableLayout(drawer)
                .build();
        //on item menu selected print hello

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        TextView navHeaderName = navigationView.getHeaderView(0).findViewById(R.id.User_name);
        User user = Login.getUserFromSharedPreferences(this);
        navHeaderName.setText(user.getFirstName() + " " + user.getLastName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.nav_logout) {
//            // Handle the logout click here
//            Log.d("Logout", "User pressed Logout");
//
//            // TODO
//            Login.removeSharedPreferences(this);
//
//            Intent intent = new Intent(navDrawer.this, MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            // Add your logout logic here, such as clearing shared preferences or performing other actions
//            return true;
//        }

        // Handle other navigation item clicks

        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}