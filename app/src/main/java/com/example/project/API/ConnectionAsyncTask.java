package com.example.project.API;
import android.app.Activity;
import android.os.AsyncTask;

import com.example.project.Objects.Car;
import com.example.project.Screens.Connect_welcome.MainActivity;

import java.util.List;
public class ConnectionAsyncTask extends AsyncTask<String, String,String> {

    Activity activity;
    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        ((MainActivity) activity).setButtonText("connecting");
        super.onPreExecute();
        ((MainActivity) activity).setProgress(true);
    }
    @Override
    protected String doInBackground(String... params) {
        String data = HttpManager.getData(params[0]);
        return data;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((MainActivity) activity).setProgress(false);
        ((MainActivity) activity).setButtonText("connected");
        List<Car> cars = MainJsonParser.extractCarsFromJson(s);
        ((MainActivity) activity).fillCars(cars);
    }
}