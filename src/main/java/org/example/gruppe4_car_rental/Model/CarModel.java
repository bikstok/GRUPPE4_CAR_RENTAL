package org.example.gruppe4_car_rental.Model;

public class CarModel {
    private String model;
    private  String brand;

    public CarModel(){
    }

    public CarModel(String model, String brand) {
        this.model = model;
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
