package com.main.project.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.main.project.Screens.Utilities.CarUtility;

public class FavoritesFragment extends Fragment {

    LinearLayout all_cars_view;
    DataBaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_your_favorites, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        all_cars_view = root.findViewById(R.id.all_cars_view);

        // user
        User user = Login.getUserFromSharedPreferences(getContext());

        // Call the viewSpecificCars method
        CarUtility.viewSpecificCars(all_cars_view, dbHelper.getFavoriteCars(user.getEmail()), requireContext());

        return root;
    }
}
