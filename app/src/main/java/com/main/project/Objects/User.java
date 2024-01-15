package com.main.project.Objects;

import java.util.List;

public class User {
    private boolean rememberMe;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String password;
    private String country;
    private String city;
    private String phoneNumber;
    private List<Car> favoriteCars;  // New field to represent favorite cars

    // Constructor
    public User(String email, String firstName, String lastName, String gender, String password,
                String country, String city, String phoneNumber, boolean rememberMe, List<Car> favoriteCars) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.password = password;
        this.country = country;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.rememberMe = rememberMe;
        this.favoriteCars = favoriteCars;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Car> getFavoriteCars() {
        return favoriteCars;
    }

    public void setFavoriteCars(List<Car> favoriteCars) {
        this.favoriteCars = favoriteCars;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
