package com.example.project.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Objects.Car;
import com.example.project.R;
import com.squareup.picasso.Picasso;

public class Car_Menu_details extends Fragment {

    private TextView carNameTextView;
    private TextView carTypeTextView;
    private TextView carModelTextView;
    private TextView carYearTextView;
    private TextView carPriceTextView;
    private ImageView carImageView;

    public Car_Menu_details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car__menu_details, container, false);

        carNameTextView = view.findViewById(R.id.view_Name);
        carTypeTextView = view.findViewById(R.id.view_Type);
        carModelTextView = view.findViewById(R.id.view_Model);
        carYearTextView = view.findViewById(R.id.view_Year);
        carPriceTextView = view.findViewById(R.id.view_Price);
        carImageView = view.findViewById(R.id.view_image);

        Car car = (Car) getArguments().getSerializable("carObject");

        carNameTextView.setText(car.getBrand());
        carTypeTextView.setText(car.getType());
        carModelTextView.setText(car.getModel());
        carYearTextView.setText(car.getYear());

        String price_str =Integer.toString((int) car.getPrice());
        carPriceTextView.setText(price_str+" $");
        Picasso.get().load(car.getImage()).into(carImageView);

        return view;
    }

    private void loadImage(ImageView imageView, String imagePath) {
        // Load image using Picasso or any other image loading library
        Picasso.get().load(imagePath).into(imageView);
    }
}