package com.example.project.Objects;

import java.io.Serializable;

public class Reservation implements Serializable {
    private String userEmail;
    private Car car;
    private String reservationDate;

    public Reservation(String userEmail, Car car, String reservationDate) {
        this.userEmail = userEmail;
        this.car = car;
        this.reservationDate = reservationDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "userEmail='" + userEmail + '\'' +
                ", car=" + car +
                ", reservationDate='" + reservationDate + '\'' +
                '}';
    }
}
