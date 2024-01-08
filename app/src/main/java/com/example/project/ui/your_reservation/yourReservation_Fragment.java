package com.example.project.ui.your_reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.Database.DataBaseHelper;
import com.example.project.Objects.User;
import com.example.project.R;
import com.example.project.Screens.Auth.Login;
import com.example.project.Screens.Utilities.CarUtility;

public class yourReservation_Fragment extends Fragment {

    LinearLayout all_cars_view;
    DataBaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_your_reservation, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        all_cars_view = root.findViewById(R.id.all_cars_view);

        // user
        User user = Login.getUserFromSharedPreferences(getContext());


        // Call the viewSpecificCars method
        CarUtility.viewSpecificCars(all_cars_view, dbHelper.getUserReservations(user.getEmail()), requireContext(), Boolean.TRUE);

        return root;
    }
}
