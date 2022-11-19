package com.openclassrooms.go4lunch.models;

public class Lunch {

    private String dateLunch;
    private Restaurant restaurantChoosed;
    private Workmate workmates;

    public Lunch() {
    }

    public Lunch(String dateLunch, Restaurant restaurantChoosed, Workmate workmates) {
        this.dateLunch = dateLunch;
        this.restaurantChoosed = restaurantChoosed;
        this.workmates = workmates;
    }
    public String getDateLunch() {

        return dateLunch;
    }
    public Restaurant getRestaurantChoosed() {

        return restaurantChoosed;
    }
    public Workmate getWorkmates() {

        return workmates;
    }
    // --- SETTERS ---

    public void setDateLunch(String dateLunch) {

        this.dateLunch = dateLunch;
    }
    public void setRestaurantChoosed(Restaurant restaurantChoosed) {
        this.restaurantChoosed = restaurantChoosed;
    }
    public void setWorkmates(Workmate workmates) {

        this.workmates = workmates;
    }

}
