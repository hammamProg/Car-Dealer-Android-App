package com.main.project.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.main.project.Database.DataBaseHelper;
import com.main.project.R;
import com.main.project.Screens.Utilities.CarUtility;

import java.util.stream.Collectors;

public class CarMenuFragment extends Fragment {

    LinearLayout all_cars_view;
    DataBaseHelper dbHelper;
    int min_price = -1;
    int max_price = -1;
    int year = -1;
    String name = null;
    String model = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_car_menu, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        all_cars_view = root.findViewById(R.id.all_cars_view);
        EditText search_bar = root.findViewById(R.id.search_bar);
        // Call the viewSpecificCars method
        CarUtility.viewSpecificCars(all_cars_view, dbHelper.getAllCars(), requireContext());
        ImageView search_icon = root.findViewById(R.id.search_icon);
        search_icon.setOnClickListener(v -> {
            // Call the searchCars method
            name = search_bar.getText().toString();
            all_cars_view.removeAllViews();
            CarUtility.viewSpecificCars(all_cars_view, dbHelper.getAllCars() .stream().filter(car -> {
                if (min_price != -1 && car.getPrice() < min_price) return false;
                if (max_price != -1 && car.getPrice() > max_price) return false;
                if (year != -1 && Integer.parseInt(car.getYear()) != year) return false;
                if (name != null && !(car.getBrand()+" "+car.getModel()).toLowerCase().contains(name.toLowerCase())) return false;
                if (model != null && !car.getModel().toLowerCase().contains(model.toLowerCase())) return false;
                return true;
                    }).collect(Collectors.toList()), requireContext());

        });
        ImageView settings_icon = root.findViewById(R.id.settings_icon);
        settings_icon.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            MenuInflater inflater2 = popup.getMenuInflater();
            inflater2.inflate(R.menu.search_settings_menu, popup.getMenu());

            // Define the click listener for menu items
            popup.setOnMenuItemClickListener(item -> {
// Handle menu items
                // open another pop up window for reading the options
                if(item.getItemId()==R.id.action_option1){
                    final EditText input = new EditText(getContext());

                    // Set the input type for the EditText (optional, depending on your needs)
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Enter the minimum price");
                    builder.setMessage("Enter the minimum price");
                    builder.setCancelable(true);
                    builder.setView(input)
                            // Add the "OK" button and its action
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                String userInput = input.getText().toString();
                                if(userInput.isEmpty()) min_price = -1;
                                else min_price = Integer.parseInt(userInput);
                            });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    builder.show();
                    return true;
                }
                else if (item.getItemId()==R.id.action_option2){
                    final EditText input = new EditText(getContext());

                    // Set the input type for the EditText (optional, depending on your needs)
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Enter the maximum price");
                    builder.setMessage("Enter the maximum price");
                    builder.setCancelable(true);
                    builder.setView(input)
                            // Add the "OK" button and its action
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                String userInput = input.getText().toString();
                                if(userInput.isEmpty()) max_price = -1;
                                else max_price = Integer.parseInt(userInput);
                            });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    builder.show();
                    return true;
                }
                else if (item.getItemId()==R.id.action_option3){
                    final EditText input = new EditText(getContext());

                    // Set the input type for the EditText (optional, depending on your needs)
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Enter the year");
                    builder.setMessage("Enter the year");
                    builder.setCancelable(true);
                    builder.setView(input)
                            // Add the "OK" button and its action
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                String userInput = input.getText().toString();
                                if(userInput.isEmpty()) year = -1;
                                else year = Integer.parseInt(userInput);
                            });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    builder.show();
                    return true;
                } else if (item.getItemId()==R.id.action_option4){
                    final EditText input = new EditText(getContext());

                    // Set the input type for the EditText (optional, depending on your needs)
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Enter the model");
                    builder.setMessage("Enter the model");
                    builder.setCancelable(true);
                    builder.setView(input)
                            // Add the "OK" button and its action
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                String userInput = input.getText().toString();
                                if(userInput.isEmpty()) model = null;
                                else model = userInput;
                            });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    builder.show();
                    return true;
                }else {
                    return false;
                }


            });

            // Show the menu
            popup.show();
        });
        return root;
    }

}
