package com.example.project.Screens.Connect_welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project.API.ConnectionAsyncTask;
import com.example.project.Database.DataBaseHelper;
import com.example.project.Objects.Car;
import com.example.project.R;
import com.example.project.Screens.Auth.Login;
import com.example.project.ui.CarUtility;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    DataBaseHelper dbHelper;
    MediaPlayer mediaPlayer;
    ImageView logo_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DataBaseHelper(this);

        setProgress(false);
        button = (Button) findViewById(R.id.connectButton);

        mediaPlayer = MediaPlayer.create(this, R.raw.car_engine_starting);
        logo_image = findViewById(R.id.logoImageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this);
                connectionAsyncTask.execute("https://6598e4f4a20d3dc41cef093f.mockapi.io/cars");

                mediaPlayer.start();

                // Animation
                ImageView logo_image = findViewById(R.id.logoImageView);
                Animation scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_animation);
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Animation started
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Animation finished
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Start the next activity after a 1-second delay
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }, 1000); // 1000 milliseconds = 1 second
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Animation repeated
                    }
                });

                logo_image.startAnimation(scaleAnimation);
            }
        });

    }
    public void setButtonText(String text) {
        button.setText(text);
    }

    public void fillCars(List<Car> cars) {
        // Fill all cars into database

//        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.testData);
//        linearLayout.removeAllViews();

        for (int i = 0; i < cars.size(); i++) {
            if (dbHelper.checkCarExistence(cars.get(i).getId())){
                break;
            }

            dbHelper.addCar(cars.get(i));

//            TextView textView = new TextView(this);
//            textView.setText(cars.get(i).toString());
//            linearLayout.addView(textView);
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