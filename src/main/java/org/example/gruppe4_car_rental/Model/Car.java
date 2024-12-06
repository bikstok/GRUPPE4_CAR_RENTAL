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
    private double original_price
        ;

    public Car(){

    }

    public Car(String frame_number, String brand, String model, String car_status, String fuel_type, String gear_type, int year_produced, double monthly_sub_price, int odometer, double original_price) {
        this.frame_number = frame_number;
        this.brand =brand;
        this.model = model;
        this.car_status = car_status;
        this.fuel_type = fuel_type;
        this.gear_type = gear_type;
        this.year_produced = year_produced;
        this.monthly_sub_price = monthly_sub_price;
        this.odometer = odometer;
        this.original_price = original_price;
    }

    @Override
    public String toString() {
        return this.frame_number + " " + this.brand + " " + this.model;
    }

    public String getFrame_number() {
        return frame_number;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getCar_status() {
        return car_status;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public String getGear_type() {
        return gear_type;
    }

    public int getYear_produced() {
        return year_produced;
    }

    public double getMonthly_sub_price() {
        return monthly_sub_price;
    }

    public int getOdometer() {
        return odometer;
    }

    public double getOriginal_price() {
        return original_price;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof Car) {
            Car car = (Car) object;
            return this.getFrame_number().equals(car.getFrame_number());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getFrame_number().hashCode();
    }

}
