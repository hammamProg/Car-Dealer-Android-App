package com.main.project.Screens.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.Car;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.squareup.picasso.Picasso;

public class CarMenuDetailsOffers extends Fragment {

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
    public CarMenuDetailsOffers(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_menu_details_discount, container, false);

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
        String price_with_discount_str = Integer.toString((int) ( (car.getDiscount()/100 )* car.getPrice()));
        carPriceTextView.setText(price_str+"$ -> "+price_with_discount_str+"$");
        Picasso.get().load(car.getImage()).into(carImageView);

        if(dbHelper.checkIfCarIsInFavorite(Login.getUserFromSharedPreferences(context).getEmail(), car.getId())){
            likeButton.setImageResource(R.drawable.favourite_icon);
        }else{
            likeButton.setImageResource(R.drawable.unfavourite_icon);
        }


        // => like button
        likeButton.setOnClickListener(v -> {
            // Call the addFavoriteCar method when the button is clicked
            User user = Login.getUserFromSharedPreferences(context);
            if(dbHelper.checkIfCarIsInFavorite(user.getEmail(), car.getId())){
                long result = dbHelper.deleteFavoriteCar(user.getEmail(), car.getId());
                if (result != -1) {
                    likeButton.setImageResource(R.drawable.unfavourite_icon);
                } else {
                    Log.d("error"," removing favorite car");
                }
            }else{
                long result = dbHelper.addFavoriteCar(user.getEmail(), car.getId());
                if (result != -1) {
                    Toast.makeText(context, "Car added to favorite list", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("error"," adding favorite car");
                }
            }
            if(dbHelper.checkIfCarIsInFavorite(Login.getUserFromSharedPreferences(context).getEmail(), car.getId())){
                likeButton.setImageResource(R.drawable.favourite_icon);
            }else{
                likeButton.setImageResource(R.drawable.unfavourite_icon);
            }


        });

        // => reserve button
        reserveButton.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm Reservation");
            builder.setMessage("Are you sure you want to reserve this car for "+car.getPrice()+"$?");
            // Set up the buttons
            builder.setPositiveButton("Confirm", (dialog, which) -> {
                User user = Login.getUserFromSharedPreferences(context);
                long result = dbHelper.reserveCar(user.getEmail(), car.getId());
                if (result != -1) {
                    Toast.makeText(context, "Car reserved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("error","reserving a car");
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        return view;
    }

    private void loadImage(ImageView imageView, String imagePath) {
        // Load image using Picasso
        Picasso.get().load(imagePath).into(imageView);
    }
}