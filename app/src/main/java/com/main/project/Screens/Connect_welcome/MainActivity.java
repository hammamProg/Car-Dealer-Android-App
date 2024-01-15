package com.main.project.Screens.Connect_welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.main.project.API.ConnectionAsyncTask;
import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.Car;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;
import com.main.project.navDrawer;

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
        button.setOnClickListener(v -> {
            ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this);
            connectionAsyncTask.execute(getResources().getString(R.string.api_cars));

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
        });

        // TODO -> implement the logout functionality & auto-logout after n-days of remember me
        User user = Login.getUserFromSharedPreferences(this);
        if (user != null){
            // there's user in sharedPreference
            Intent intent = new Intent(MainActivity.this, navDrawer.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            // there's no user ( Don't do anything )
            Log.d("SharedMemory", "user is null");
        }

    }
    public void setButtonText(String text) {
        button.setText(text);
    }

    public void fillCars(List<Car> cars) {
        // Fill all cars into database

        for (int i = 0; i < cars.size(); i++) {
            if (dbHelper.checkCarExistence(cars.get(i).getId())){
                break;
            }
            dbHelper.addCar(cars.get(i));
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