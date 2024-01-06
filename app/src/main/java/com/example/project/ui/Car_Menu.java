package com.example.project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.project.Database.DataBaseHelper;
import com.example.project.Objects.Car;
import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Car_Menu extends AppCompatActivity {
//  TODO -> ADD Search functionality



    LinearLayout all_cars_view;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_menu);


        dbHelper = new DataBaseHelper(this);
        all_cars_view = findViewById(R.id.all_cars_view);

        // Call the viewSpecificCars method
        CarUtility.viewSpecificCars(all_cars_view, dbHelper.getAllCars(), this);


    }


}