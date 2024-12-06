package org.example.gruppe4_car_rental.Model;

public class DamageType {

    private String damage_name;
    private double damage_price;

    public DamageType(String damage_name, double damage_price) {
        this.damage_name = damage_name;
        this.damage_price = damage_price;
    }

    public String getDamage_name() {
        return damage_name;
    }

    public void setDamage_name(String damage_name) {
        this.damage_name = damage_name;
    }

    public double getDamage_price() {
        return damage_price;
    }

    public void setDamage_price(double damage_price) {
        this.damage_price = damage_price;
    }
}
