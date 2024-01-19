package com.main.project.Screens.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Connect_welcome.MainActivity;
import com.main.project.UI.NavDrawer;

import com.google.gson.Gson;

public class Login extends AppCompatActivity {
    private EditText email, password;
    private CheckBox checkBoxRememberMe;
    private Button buttonLogin;
    public static String sharedMemoryName = "UserShared";
    private static DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DataBaseHelper(this);
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

        buttonLogin.setOnClickListener(view -> login(checkBoxRememberMe.isChecked()));

        // TODO -> implement the logout functionality & auto-logout after n-days of remember me
        User user = Login.getUserFromSharedPreferences(this);
        if (user != null && user.isRememberMe()){
            // there's user in sharedPreference
            Intent intent = new Intent(Login.this, NavDrawer.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            // there's no user ( Don't do anything )
            Log.d("SharedMemory", "user is null");
        }
    }

    public void login(boolean checked){
        String email_s = email.getText().toString().trim();
        String password_s = password.getText().toString();

        boolean log_in_success = dbHelper.loginUser(email_s,password_s, checked);

        if (log_in_success){
            // Logged in success
            Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

            // -> get user data as User object
            saveUserToSharedPreferences(this,email_s);

            Intent intent = new Intent(Login.this, NavDrawer.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }else{
            // Logged in failed
            Toast.makeText(this, "Logged In Failed!! Incorrect credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void saveUserToSharedPreferences(Context context, String email) {
        User user = dbHelper.getUserByEmail(email);

        SharedPreferences preferences = context.getSharedPreferences(sharedMemoryName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert User object to JSON string
        String userJson = new Gson().toJson(user);

        editor.putString("userJson", userJson);
        // editor.putBoolean("rememberMe", true);  // Uncomment if needed
        editor.apply();
    }

    public static User getUserFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(sharedMemoryName, Context.MODE_PRIVATE);

        // Retrieve JSON string from SharedPreferences
        String userJson = preferences.getString("userJson", null);

        // Convert JSON string back to User object
        if (userJson != null) {
            return new Gson().fromJson(userJson, User.class);
        } else {
            return null;
        }
    }

    public static void clearUserSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(sharedMemoryName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Remove saved data
        editor.remove("userJson");
//        editor.remove("rememberMe");

        // Apply changes
        editor.apply();
    }

}