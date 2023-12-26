package com.example.project.API;

import com.example.project.Objects.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarJsonParser {

    public static List<Car> getObjectFromJson(String json){
        List<Car> cars;

        try{
            JSONArray jsonArray = new JSONArray(json);
            cars = new ArrayList<>();

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);

                Car car = new Car();
                car.setId(jsonObject.getInt("id"));
                car.setType(jsonObject.getString("type"));

                cars.add(car);
            }

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return cars;
    }
}
