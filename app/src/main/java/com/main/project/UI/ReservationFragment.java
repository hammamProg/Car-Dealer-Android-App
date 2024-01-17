package com.main.project.UI;

import static com.main.project.Screens.Utilities.CarUtility.replaceParentWithElements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.Car;
import com.main.project.Objects.User;
import com.main.project.R;

import com.main.project.Screens.Auth.Login;
import com.main.project.Screens.Utilities.CarUtility;

import java.util.List;

public class ReservationFragment extends Fragment {

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


        // check if there're any reservation, if non add @drawable/man ImageView + "it looks like you haven't added any favorite items to your list yet" TextView
        List<Car> reservation_cars = dbHelper.getUserReservations(user.getEmail());

        if (reservation_cars.isEmpty()) {
            replaceParentWithElements(requireContext(), all_cars_view, R.drawable.man, "It looks like you haven't added any reservation to your list yet !!");
        }else{
            // Call the viewSpecificCars method
            CarUtility.viewSpecificCars(all_cars_view, reservation_cars, requireContext(),Boolean.TRUE);
        }


        return root;
    }
}
