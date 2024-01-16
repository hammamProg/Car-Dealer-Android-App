package com.main.project.Screens.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.Car;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;

public class CarUtility {
    static boolean isReservationValue ;

    public static void viewSpecificCars(LinearLayout allCarsView, List<Car> allCars, Context context, Boolean... isReservation) {
        allCarsView.removeAllViews();
        // Check if isReservation is provided
        if (isReservation.length > 0) {
            isReservationValue = isReservation[0];
        }else{isReservationValue=false;}


        if (isReservationValue){
            for (int i = 0; i < allCars.size(); i++) {
                LinearLayout linearLayout1 = createLinearLayout(context);

                addCarReservedView(linearLayout1, allCars.get(i), context);
                allCarsView.addView(linearLayout1);

                View space = new View(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        dpToPx(context, 8), ViewGroup.LayoutParams.MATCH_PARENT);
                space.setLayoutParams(layoutParams);

                allCarsView.addView(space);
            }

        }else{

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
                linearLayout1.setOnClickListener(v -> showCarDetails(allCars.get(finalI), context));
            }
        }




    }

    private static void showCarDetails(Car car, Context context) {
        Car_Menu_details fragment = new Car_Menu_details(context);
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

    private static void addCarReservedView(LinearLayout linearLayout, Car car, Context context) {
        // Create the main LinearLayout for the card
        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cardLayout.setGravity(Gravity.CENTER);
        cardLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create the ImageView for the car image
        ImageView imageView = new ImageView(context);
        int imageWidth = dpToPx(context, 200);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                imageWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        loadImage(imageView, car.getImage(), context);

        // Create the LinearLayout for car details
        LinearLayout detailsLayout = new LinearLayout(context);
        detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                dpToPx(context, 100), ViewGroup.LayoutParams.WRAP_CONTENT));
        detailsLayout.setOrientation(LinearLayout.VERTICAL);

        // Create TextViews for car details
        TextView carNameTextView = createTextView(context, car.getBrand());
        TextView carTypeTextView = createTextView(context, car.getType());
        TextView carModelTextView = createTextView(context, car.getModel());
        TextView carYearTextView = createTextView(context, car.getYear());
        TextView carPriceTextView = createTextView(context, String.format(Locale.getDefault(), "%.0f $", car.getPrice()));

        // Add TextViews to detailsLayout
        detailsLayout.addView(carNameTextView);
        detailsLayout.addView(carTypeTextView);
        detailsLayout.addView(carModelTextView);
        detailsLayout.addView(carYearTextView);
        detailsLayout.addView(carPriceTextView);

        // Create TextView for reservation date
        TextView reservationDateTextView = createTextView(context, car.getReservationDate());
        reservationDateTextView.setId(View.generateViewId()); // Assign a unique ID for later reference
        reservationDateTextView.setPadding(5,0,5,0);
        reservationDateTextView.setGravity(Gravity.CENTER);

        //create button to end reservation
        Button endReservationButton = new Button(context);
        endReservationButton.setText("End Reservation");
        endReservationButton.setBackgroundResource(R.drawable.view_background);
        endReservationButton.setTextColor(Color.parseColor("#FFFFFF"));

        // Add views to cardLayout
        cardLayout.addView(imageView);
        cardLayout.addView(detailsLayout);
        addSpacer(cardLayout, 10, 0, context); // Add a spacer with 8dp width and 0dp height
        // Add a vertical linear layout with button underneath the reservation date
        LinearLayout buttonLayout = new LinearLayout(context);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonLayout.setOrientation(LinearLayout.VERTICAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.addView(reservationDateTextView);
        buttonLayout.addView(endReservationButton);
        // Add the buttonLayout to the cardLayout
        cardLayout.addView(buttonLayout);

        endReservationButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm End Reservation");
            builder.setMessage("Are you sure you want to end the reservation of this car?");
            // Set up the buttons
            builder.setPositiveButton("Confirm", (dialog, which) -> {
                User user = Login.getUserFromSharedPreferences(context);
                DataBaseHelper dbHelper = new DataBaseHelper(context);
                long result = dbHelper.endReservation(user.getEmail(), car.getId());
                if (result != -1) {
                    Toast.makeText(context, "Car reservation ended successfully", Toast.LENGTH_SHORT).show();
                    linearLayout.removeView(cardLayout);
                } else {
                    Log.d("error","Ending reservation a car");
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        // Add the cardLayout to the main linearLayout
        linearLayout.addView(cardLayout);
    }

    private static TextView createTextView(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(5, 5, 5, 5);
        return textView;
    }

    private static void addSpacer(LinearLayout linearLayout, int widthInDp, int heightInDp, Context context) {
        Space spacer = new Space(context);

        int widthInPixels = dpToPx(context, widthInDp);
        int heightInPixels = dpToPx(context, heightInDp);

        spacer.setLayoutParams(new LinearLayout.LayoutParams(widthInPixels, heightInPixels));

        linearLayout.addView(spacer);
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
