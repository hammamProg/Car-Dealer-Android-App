package com.main.project.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.main.project.API.HttpManager;
import com.main.project.API.MainJsonParser;
import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.main.project.Screens.Auth.SignUp;
import com.main.project.Screens.Utilities.CarUtility;
import com.main.project.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ProfileFragment extends Fragment {

    private EditText firstName,lastName,password,confirmPassword,phone;
    private Spinner genderSpinner, countrySpinner, citySpinner;
    //    private CheckBox termsCheckBox; //TODO
    private Button saveButton;
    private TextView phoneNumberTextView;
    User user;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        genderSpinner = root.findViewById(R.id.genderSpinner);
        countrySpinner = root.findViewById(R.id.countrySpinner);
        citySpinner = root.findViewById(R.id.citySpinner);
        phoneNumberTextView = root.findViewById(R.id.ZipAreaTextView);

        user = Login.getUserFromSharedPreferences(requireContext());
        firstName = root.findViewById(R.id.firstNameEditText);
        firstName.setText(user.getFirstName());
        lastName = root.findViewById(R.id.lastNameEditText);
        lastName.setText(user.getLastName());
        password = root.findViewById(R.id.passwordSignUpEditText);
        confirmPassword = root.findViewById(R.id.confirmPasswordEditText);
        phone = root.findViewById(R.id.phoneNumberEditText);
        //put phone from the last ')' to the end
        phone.setText(user.getPhoneNumber().substring(user.getPhoneNumber().indexOf(')')+1));
//        phone.setText(user.getPhoneNumber().substring());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setSelection(adapter.getPosition(user.getGender()));
        new Thread(() -> {
            // Perform background task
            // Simulating a time-consuming task
            String countries_json= HttpManager.getData(getResources().getString(R.string.api_countries_codes)+"/codes");
            List<CharSequence> dataList = MainJsonParser.extractCountriesFromJson(countries_json);
            System.out.println(countries_json);
            // Update the UI with the result on the main thread
            getActivity().runOnUiThread(() -> {
                assert dataList != null;
                ArrayAdapter<CharSequence> countryAdapter= new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dataList);
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countrySpinner.setAdapter(countryAdapter);
                //filter the options according to the country name
                int countryOption = IntStream.range(0, dataList.size())
                        .filter(i -> dataList.get(i).toString().startsWith(user.getCountry()))
                        .findFirst().getAsInt();
                countrySpinner.setSelection(countryAdapter.getPosition(dataList.get(countryOption)));
            });
        }).start();
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCountry = parentView.getItemAtPosition(position).toString();

                // Choose the City & Phone_start by > Chosen Country
                populateCitySpinner(selectedCountry);
                updatePhoneNumberFormat(selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });

        // #####################################################################################
        // ####################  When Signup


        saveButton = root.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(view -> {
            save();

        });
        return root;
    }

    private void save() {
        DataBaseHelper dbHelper = new DataBaseHelper(requireContext());

        String first_Name = firstName.getText().toString().trim();
        String last_Name = lastName.getText().toString().trim();
        String password_s = password.getText().toString();
        String confirm_password = confirmPassword.getText().toString();
        String phoneNumber = phone.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String country = countrySpinner.getSelectedItem().toString();
        String city = citySpinner.getSelectedItem().toString();

        // Check if all fields are filled
        if (TextUtils.isEmpty(first_Name) || TextUtils.isEmpty(last_Name) ||
                TextUtils.isEmpty(password_s) || TextUtils.isEmpty(confirm_password) || TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the password meets the criteria
        if (password_s.length() < 5 || !password_s.matches(".*\\d.*") || !password_s.matches(".*[a-zA-Z].*") || !password_s.matches(".*[!@#$%^&*()-_=+{}|;:',.<>/?`~\\[\\]\\s\"].*")) {
            Toast.makeText(requireContext(), "Password must be at least 5 characters long and include at least one letter, one number, and one special character", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password_s.equals(confirm_password)) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the phone number is valid (you may need a more robust validation based on your requirements)
        if (phoneNumber.length() < 5) {
            Toast.makeText(requireContext(), "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (first_Name.length() < 3) {
            Toast.makeText(requireContext(), "Enter a valid first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (last_Name.length() < 3) {
            Toast.makeText(requireContext(), "Enter a valid last name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if terms and conditions are accepted TODO
//        if (!termsCheckBox.isChecked()) {
//            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Now you can proceed to add the user to the database
        String phone_start = phoneNumberTextView.getText().toString();
        phoneNumber = phone_start.concat(phoneNumber);

        user.setCity(city);
        user.setCountry(country);
        user.setFirstName(first_Name);
        user.setLastName(last_Name);
        user.setGender(gender);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password_s);
        long result=dbHelper.updateUser(user);
        if (result != -1) {
            SharedPreferences preferences = getActivity().getSharedPreferences(Login.sharedMemoryName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String userJson = new Gson().toJson(user);
            editor.putString("userJson", userJson);
            editor.apply();
            Toast.makeText(getContext(), "Information Updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error updating. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private void populateCitySpinner(String selectedCountry) {

        // removing the country code from the selected country
        new Thread(() -> {
            ArrayAdapter<CharSequence> cityAdapter;
            if(selectedCountry.startsWith("Palestinian Territory")){
                cityAdapter= ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.palestine_cities,
                        android.R.layout.simple_spinner_item
                );
            }else {
                String country = selectedCountry.substring(0, selectedCountry.indexOf(","));
                String citiesJSON = HttpManager.postData(getResources().getString(R.string.api_countries_codes) + "/cities", "{\"country\":\"" + country + "\"}");
                List<CharSequence> dataList = MainJsonParser.extractCitiesFromJson(citiesJSON);
                assert dataList != null;
                cityAdapter= new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dataList);
            }
            // Update the UI with the result on the main thread
            requireActivity().runOnUiThread(() -> {
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);
                //true if city is in the adapter
                boolean cityInAdapter = IntStream.range(0, cityAdapter.getCount())
                        .anyMatch(i -> cityAdapter.getItem(i).toString().equals(user.getCity()));
                if(cityInAdapter){
                    citySpinner.setSelection(cityAdapter.getPosition(user.getCity()));
                }

            });
        }).start();

    }

    private void updatePhoneNumberFormat(String selectedCountry) {
        String phoneNumberFormat = getPhoneNumberFormat(selectedCountry);
        phoneNumberTextView.setText(phoneNumberFormat);
    }

    private String getPhoneNumberFormat(String selectedCountry) {
        String[] tokens= selectedCountry.split(",");
        return "("+tokens[tokens.length-1]+")";
    }
}