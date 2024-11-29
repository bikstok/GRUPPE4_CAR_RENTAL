package org.example.gruppe4_car_rental.Model;

public class Car {
    private String frame_number;
    private String model;
    private String brand;
    private String car_status;
    private String fuel_type;
    private String gear_type;
    private int year_produced;
    private double monthly_sub_price;
    private int odometer;
    private double orignal_price;

    public Car(){

    }

    public Car(String frame_number, String brand, String model, String car_status, String fuel_type, String gear_type, int year_produced, double monthly_sub_price, int odometer, double orignal_price) {
        this.frame_number = frame_number;
        this.brand =brand;
        this.model = model;
        this.car_status = car_status;
        this.fuel_type = fuel_type;
        this.gear_type = gear_type;
        this.year_produced = year_produced;
        this.monthly_sub_price = monthly_sub_price;
        this.odometer = odometer;
        this.orignal_price = orignal_price;
    }

    @Override
    public String toString() {
        return this.brand + " " + this.model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFrame_number() {
        return frame_number;
    }

    public void setFrame_number(String frame_number) {
        this.frame_number = frame_number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCar_status() {
        return car_status;
    }

    public void setCar_status(String car_status) {
        this.car_status = car_status;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getGear_type() {
        return gear_type;
    }

    public void setGear_type(String gear_type) {
        this.gear_type = gear_type;
    }

    public int getYear_produced() {
        return year_produced;
    }

    public void setYear_produced(int year_produced) {
        this.year_produced = year_produced;
    }

    public double getMonthly_sub_price() {
        return monthly_sub_price;
    }

    public void setMonthly_sub_price(double monthly_sub_price) {
        this.monthly_sub_price = monthly_sub_price;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public double getOrignal_price() {
        return orignal_price;
    }

    public void setOrignal_price(double orignal_price) {
        this.orignal_price = orignal_price;
    }
}
