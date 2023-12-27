package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project.API.ConnectionAsyncTask;
import com.example.project.Database.DataBaseHelper;
import com.example.project.Objects.Car;
import com.example.project.Screens.Auth.Login;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseHelper dbHelper = new DataBaseHelper(this);

        setProgress(false);
        button = (Button) findViewById(R.id.connectButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this);
                connectionAsyncTask.execute("https://658582eb022766bcb8c8c86e.mockapi.io/api/mock/rest-apis/encs5150/car-types");
                // TODO > if    connection success trans.to. Login
                //      > else  stay in the same page



                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        linearLayout = (LinearLayout) findViewById(R.id.testData);
    }
    public void setButtonText(String text) {
        button.setText(text);
    }

    public void fillStudents(List<Car> cars) {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.testData);
        linearLayout.removeAllViews();
        for (int i = 0; i < cars.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(cars.get(i).toString());
            linearLayout.addView(textView);
        }
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}