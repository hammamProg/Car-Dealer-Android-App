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

    LinearLayout all_cars_view;
    DataBaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_menu);

        all_cars_view = findViewById(R.id.all_cars_view);
        all_cars_view.removeAllViews();

        dbHelper = new DataBaseHelper(this);

        List<Car> all_cars = dbHelper.getAllCars();

        for (int i = 0; i < all_cars.size(); i++) {
            LinearLayout linearLayout1 = createLinearLayout(this);

            addCarView(linearLayout1, all_cars.get(i));
            all_cars_view.addView(linearLayout1);

            View space = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    dpToPx(8),ViewGroup.LayoutParams.MATCH_PARENT);  // Adjust the height of the space as needed
            space.setLayoutParams(layoutParams);

            all_cars_view.addView(space);

            // Set click listener for each generated card view
            int finalI = i;
            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the click event and show the CarDetailsFragment
                    showCarDetails(all_cars.get(finalI));
                }
            });

        }


    }

    private void showCarDetails(Car car) {
        Car_Menu_details fragment = new Car_Menu_details();
        Bundle args = new Bundle();

        args.putSerializable("carObject", car);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.pressed_car_view, fragment);
        transaction.addToBackStack(null);  // Optional: Add transaction to back stack
        transaction.commit();
    }

    private LinearLayout createLinearLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(getResources().getDrawable(R.drawable.customborder)); // Set background drawable
        return linearLayout;
    }

    private void addCarView(LinearLayout linearLayout, Car car) {
        ImageView imageView = new ImageView(this);
        int imageWidth = dpToPx(120);  // Set your desired width here
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                imageWidth, dpToPx(95)));
        // Assuming you have a method in Car class to get the image resource ID
        loadImage(imageView, car.getImage());

        TextView carNameTextView = new TextView(this);
        carNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        carNameTextView.setText(car.getBrand());

        TextView carTypeTextView = new TextView(this);
        carTypeTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        carTypeTextView.setText(car.getType());

        linearLayout.addView(imageView);
        linearLayout.addView(carNameTextView);
        linearLayout.addView(carTypeTextView);
    }

    private void loadImage(ImageView imageView, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            // Use Picasso to load the image into the ImageView
            Picasso.get().load(imagePath).into(imageView);
        } else {
            // If imagePath is empty or null, you can set a placeholder image or handle it as needed
            imageView.setImageResource(R.drawable.car1);
        }
    }



    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}