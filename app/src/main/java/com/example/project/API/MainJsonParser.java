package com.example.project.API;

import com.example.project.Objects.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainJsonParser {

    public static List<Car> extractCarsFromJson(String json){
        List<Car> cars;
        try{
            JSONArray jsonArray = new JSONArray(json);
            cars = new ArrayList<>();

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Car car = new Car();

                // Doctor Rest API
//                car.setId(jsonObject.getInt("id"));
//                car.setType(jsonObject.getString("type"));
//                car.setImage_path("drawable/project_logo.png");

                // Mine API
                car.setId(jsonObject.getInt("id"));
                car.setBrand(jsonObject.getString("Brand"));
                car.setModel(jsonObject.getString("Model"));
                car.setType(jsonObject.getString("Type"));
                car.setYear(jsonObject.getString("Year"));
                car.setColor(jsonObject.getString("Color"));
                car.setPrice(jsonObject.getDouble("Price"));
                car.setImage(jsonObject.getString("Image"));

                cars.add(car);
            }

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return cars;
    }
}
