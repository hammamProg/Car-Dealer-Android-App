package com.example.project.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.project.Objects.Car;
import com.example.project.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CarUtility {


    public static void viewSpecificCars(LinearLayout allCarsView, List<Car> allCars, Context context) {
        allCarsView.removeAllViews();

        for (int i = 0; i < allCars.size(); i++) {
            LinearLayout linearLayout1 = createLinearLayout(context);

            addCarView(linearLayout1, allCars.get(i), context);
            allCarsView.addView(linearLayout1);

            View space = new View(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    dpToPx(context, 8), ViewGroup.LayoutParams.MATCH_PARENT);
            space.setLayoutParams(layoutParams);

            allCarsView.addView(space);

            int finalI = i;
            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCarDetails(allCars.get(finalI), context);
                }
            });
        }
    }

    private static void showCarDetails(Car car, Context context) {
        Car_Menu_details fragment = new Car_Menu_details();
        Bundle args = new Bundle();

        args.putSerializable("carObject", car);
        fragment.setArguments(args);

        FragmentTransaction transaction = ((AppCompatActivity) context)
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.pressed_car_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private static LinearLayout createLinearLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(context.getResources().getDrawable(R.drawable.customborder));
        return linearLayout;
    }

    private static void addCarView(LinearLayout linearLayout, Car car, Context context) {
        ImageView imageView = new ImageView(context);
        int imageWidth = dpToPx(context, 120);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                imageWidth, dpToPx(context, 95)));
        loadImage(imageView, car.getImage(), context);

        TextView carNameTextView = new TextView(context);
        carNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        carNameTextView.setText(car.getBrand());

        TextView carTypeTextView = new TextView(context);
        carTypeTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        carTypeTextView.setText(car.getType());

        linearLayout.addView(imageView);
        linearLayout.addView(carNameTextView);
        linearLayout.addView(carTypeTextView);
    }

    private static void loadImage(ImageView imageView, String imagePath, Context context) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Picasso.get().load(imagePath).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.car1);
        }
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


}
