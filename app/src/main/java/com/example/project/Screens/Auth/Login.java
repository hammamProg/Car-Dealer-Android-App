package com.example.project.Screens.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Database.DataBaseHelper;
import com.example.project.Objects.User;
import com.example.project.R;
import com.example.project.Home;
import com.example.project.ui.Car_Menu;

import java.util.List;

public class Login extends AppCompatActivity {
    private EditText email, password;
    private CheckBox checkBoxRememberMe;
    private Button buttonLogin, buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        checkBoxRememberMe = findViewById(R.id.rememberCheckBox);
        buttonLogin = findViewById(R.id.loginButton);

        TextView signupTextView = findViewById(R.id.signupTextView);
        String textViewText = signupTextView.getText().toString();
        SpannableString spannableString = new SpannableString(textViewText);

        // Find the start index of the last word (Signup in this case)
        int startIndex = textViewText.lastIndexOf("Signup");

        // Create a ClickableSpan for the last word
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(Login.this, SignUp.class);
                 startActivity(intent);
                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, startIndex + "Signup".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupTextView.setMovementMethod(LinkMovementMethod.getInstance());
        signupTextView.setText(spannableString);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login(checkBoxRememberMe.isChecked());
            }
        });

        loadCredentialsFromSharedPreferences();
    }

    @SuppressLint("NotConstructor")
    public void Login(boolean checked){
        DataBaseHelper dbHelper = new DataBaseHelper(this);

        // For Testing issues
        List<User> allUsers = dbHelper.getAllUsers();
        for (User user : allUsers) {
            Log.d("User Info", "Email: " + user.getEmail() + ", Password: "+ user.getPassword() +
                    ", First Name: " + user.getFirstName() +
                    ", Last Name: " + user.getLastName() +
                    ", Gender: " + user.getGender() +
                    ", Country: " + user.getCountry() +
                    ", City: " + user.getCity() +
                    ", Phone Number: " + user.getPhoneNumber());
        }

        String email_s = email.getText().toString().trim();
        String password_s = password.getText().toString();

        // Check if the email is valid
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_s).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the password meets the criteria
        if (password_s.length() < 5) {
            Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = dbHelper.loginUser(email_s,password_s);

        if (result){
            // Logged in success
            Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

            // check if checked > then save the data of the user in shared preferences
            saveCredentialsToSharedPreferences(email_s, password_s, checked);


            Intent intent = new Intent(Login.this, Car_Menu.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }else{
            // Logged in failed
            Toast.makeText(this, "Logged In Failed!! check your data", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void saveCredentialsToSharedPreferences(String email, String password, boolean rememberMe) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();
    }

    private void loadCredentialsFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean rememberMe = preferences.getBoolean("rememberMe", false);

        if (rememberMe) {
            String userEmail = preferences.getString("email", "");
            String storedPassword = preferences.getString("password", "");

            // Fill in the email and password fields in the login screen
            email.setText(userEmail);
            password.setText(storedPassword);
        }
    }

}