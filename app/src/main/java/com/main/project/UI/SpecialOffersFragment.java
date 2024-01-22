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
import com.main.project.R;
import com.main.project.Screens.Utilities.CarUtility;

import java.util.List;

public class SpecialOffersFragment extends Fragment {

    LinearLayout all_cars_view;
    DataBaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_special_offers, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        all_cars_view = root.findViewById(R.id.all_cars_view);

        // Call the viewSpecificCars method

        List<Car> cars_offers = dbHelper.getCarsWithDiscountGreaterThanZero();

        if (cars_offers.isEmpty()) {
            replaceParentWithElements(requireContext(), all_cars_view, R.drawable.man, "There's no offers currently, will be added soon!");
        }else{
            // Call the viewSpecificCars method
            CarUtility.viewSpecificCars(all_cars_view, cars_offers, requireContext(),2);
        }
        return root;
    }
}
