package com.main.project.Screens.Utilities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.Car;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.squareup.picasso.Picasso;

public class Car_Menu_details extends Fragment {

    private TextView carNameTextView;
    private TextView carTypeTextView;
    private TextView carModelTextView;
    private TextView carYearTextView;
    private TextView carPriceTextView;
    private ImageView carImageView;

    // for reservation screen:
    private TextView carReservationDateTextView;

    private ImageView likeButton;
    private AppCompatButton reserveButton;
    private Context context;
    public Car_Menu_details(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_menu_details, container, false);

        DataBaseHelper dbHelper = new DataBaseHelper(context);

        carNameTextView = view.findViewById(R.id.view_Name);
        carTypeTextView = view.findViewById(R.id.view_Type);
        carModelTextView = view.findViewById(R.id.view_Model);
        carYearTextView = view.findViewById(R.id.view_Year);
        carPriceTextView = view.findViewById(R.id.view_Price);
        carImageView = view.findViewById(R.id.view_image);

        likeButton = view.findViewById(R.id.likeButton);
        reserveButton = view.findViewById(R.id.reserveButton);


        Car car = (Car) getArguments().getSerializable("carObject");


        carNameTextView.setText("Brand: "+car.getBrand());
        carTypeTextView.setText("Type: "+car.getType());
        carModelTextView.setText("Model: "+car.getModel());
        carYearTextView.setText("Year: "+car.getYear());

        String price_str =Integer.toString((int) car.getPrice());
        carPriceTextView.setText("Price: "+price_str+" $");
        Picasso.get().load(car.getImage()).into(carImageView);



        // => like button
        likeButton.setOnClickListener(v -> {
            // Call the addFavoriteCar method when the button is clicked
            User user = Login.getUserFromSharedPreferences(context);

            // TODO - logic to check if the car is liked to this user or not
            long result = dbHelper.addFavoriteCar(user.getEmail(), car.getId());
            if (result != -1) {
                Toast.makeText(context, "Car added to favorite list", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("error"," adding favorite car");
            }
        });

        // => reserve button
        reserveButton.setOnClickListener(v -> {
            // Call the addFavoriteCar method when the button is clicked
            User user = Login.getUserFromSharedPreferences(context);

            // TODO - logic to check if the car is liked to this user or not
            long result = dbHelper.reserveCar(user.getEmail(), car.getId());
            if (result != -1) {
                Toast.makeText(context, "Car reserved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("error"," adding favorite car");
            }
        });

        return view;
    }

    private void loadImage(ImageView imageView, String imagePath) {
        // Load image using Picasso
        Picasso.get().load(imagePath).into(imageView);
    }
}