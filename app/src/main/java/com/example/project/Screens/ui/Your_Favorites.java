package com.example.project.Screens.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.project.Database.DataBaseHelper;
import com.example.project.R;

public class Your_Favorites extends AppCompatActivity {

    LinearLayout all_cars_view; // Assuming this is declared somewhere in your class
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_favorites);

        // Initialize allCarsView and dbHelper if not already done
        dbHelper = new DataBaseHelper(this);
        all_cars_view = findViewById(R.id.all_cars_view);

        // Call the viewSpecificCars method
        CarUtility.viewSpecificCars(all_cars_view, dbHelper.getAllCars(), this);
    }
}
