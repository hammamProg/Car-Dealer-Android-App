package com.example.project.Objects;

import java.io.Serializable;

public class Car implements Serializable {
    private int id;
    private String brand;
    private String model;
    private String type;
    private String year;
    private String color;
    private double price;
    private String image;

    public Car(int id, String brand, String model, String type, String year, String color, double price, String image) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.year = year;
        this.color = color;
        this.price = price;
        this.image = image;
    }

    public Car() {
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public String getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", type='" + type + '\'' +
                ", year='" + year + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
