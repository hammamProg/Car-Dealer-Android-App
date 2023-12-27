package com.example.project.Screens.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;

public class Login extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private CheckBox checkBoxRememberMe;
    private Button buttonLogin, buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.emailEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
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
    }
}