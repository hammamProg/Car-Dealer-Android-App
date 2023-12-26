package com.example.project.Objects;

public class Car {
    private int id;
    private String type;

    public Car(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public Car() {
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}'+"\n";
    }

}
