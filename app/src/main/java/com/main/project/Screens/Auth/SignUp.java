package com.main.project.Screens.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.main.project.API.HttpManager;
import com.main.project.API.MainJsonParser;
import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.UI.NavDrawer;

import java.util.List;

public class SignUp extends AppCompatActivity {
    private EditText email,firstName,lastName,password,confirmPassword,phone;
    private Spinner genderSpinner, countrySpinner, citySpinner;
//    private CheckBox termsCheckBox; //TODO
    private Button signUpButton;
    private TextView phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize your views
        genderSpinner = findViewById(R.id.genderSpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        citySpinner = findViewById(R.id.citySpinner);
        phoneNumberTextView = findViewById(R.id.ZipAreaTextView);

        email = findViewById(R.id.emailEditText);
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        password = findViewById(R.id.passwordSignUpEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);
        phone = findViewById(R.id.phoneNumberEditText);

        // ======================================================================== Gender Spinner

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // ======================================================================== Country & City & Phone_zip_code Spinners
        new Thread(() -> {
            // Perform background task
            // Simulating a time-consuming task
            String countries_json=HttpManager.getData(getResources().getString(R.string.api_countries_codes)+"/codes");
            List<CharSequence> dataList = MainJsonParser.extractCountriesFromJson(countries_json);
            System.out.println(countries_json);
            // Update the UI with the result on the main thread
            runOnUiThread(() -> {
                assert dataList != null;
                ArrayAdapter<CharSequence> countryAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataList);
//                ArrayAdapter<CharSequence> countryAdapter2 = ArrayAdapter.createFromResource(
//                        this,
//                        R.array.country_options,
//                        android.R.layout.simple_spinner_item
//                );
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countrySpinner.setAdapter(countryAdapter);
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


        signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(view -> {
            //TODO > Check if the entered info are correct
            signUp();

        });




    }
    private void signUp() {
        DataBaseHelper dbHelper = new DataBaseHelper(this);

        String first_Name = firstName.getText().toString().trim();
        String last_Name = lastName.getText().toString().trim();
        String email_s = email.getText().toString().trim();
        String password_s = password.getText().toString();
        String confirm_password = confirmPassword.getText().toString();
        String phoneNumber = phone.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String country = countrySpinner.getSelectedItem().toString();
        String city = citySpinner.getSelectedItem().toString();

        // Check if all fields are filled
        if (TextUtils.isEmpty(first_Name) || TextUtils.isEmpty(last_Name) || TextUtils.isEmpty(email_s) ||
                TextUtils.isEmpty(password_s) || TextUtils.isEmpty(confirm_password) || TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the email is valid
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_s).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if the email already exists
        if (dbHelper.checkEmailExistence(email_s)) {
            // Email already exists, show an error message or handle accordingly
            Toast.makeText(this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the password meets the criteria
        if (password_s.length() < 5 || !password_s.matches(".*\\d.*") || !password_s.matches(".*[a-zA-Z].*") || !password_s.matches(".*[!@#$%^&*()-_=+{}|;:',.<>/?`~\\[\\]\\s\"].*")) {
            Toast.makeText(this, "Password must be at least 5 characters long and include at least one letter, one number, and one special character", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password_s.equals(confirm_password)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the phone number is valid (you may need a more robust validation based on your requirements)
        if (phoneNumber.length() < 5) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (first_Name.length() < 3) {
            Toast.makeText(this, "Enter a valid first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (last_Name.length() < 3) {
            Toast.makeText(this, "Enter a valid last name", Toast.LENGTH_SHORT).show();
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

        User newUser = new User(
                email_s,
                first_Name,
                last_Name,
                gender,
                password_s,
                country,
                city,
                phoneNumber,
                false,
                null
        );

        long result = dbHelper.addUser(newUser);

        if (result != -1) {
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            // You may navigate to the next screen or perform other actions
            Intent intent = new Intent(SignUp.this, NavDrawer.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else {
            Toast.makeText(this, "Error signing up. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private void populateCitySpinner(String selectedCountry) {
        Spinner citySpinner = findViewById(R.id.citySpinner);
        // removing the country code from the selected country
        new Thread(() -> {
            ArrayAdapter<CharSequence> cityAdapter;
            if(selectedCountry.startsWith("Palestinian Territory")){
                cityAdapter= ArrayAdapter.createFromResource(
                        this,
                        R.array.palestine_cities,
                        android.R.layout.simple_spinner_item
                );
            }else {
                String country = selectedCountry.substring(0, selectedCountry.indexOf(","));
                String citiesJSON = HttpManager.postData(getResources().getString(R.string.api_countries_codes) + "/cities", "{\"country\":\"" + country + "\"}");
                List<CharSequence> dataList = MainJsonParser.extractCitiesFromJson(citiesJSON);
                assert dataList != null;
                cityAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataList);
            }
            // Update the UI with the result on the main thread
            runOnUiThread(() -> {
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);
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