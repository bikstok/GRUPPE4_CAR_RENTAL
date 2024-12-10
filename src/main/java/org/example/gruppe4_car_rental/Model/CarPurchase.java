package org.example.gruppe4_car_rental.Model;

public class CarPurchase {
    private int car_purchase_id;
    private int contract_id;
    private double purchase_price;

    public CarPurchase() {
    }

    public CarPurchase(int car_purchase_id, int contract_id, double purchase_price) {
        this.car_purchase_id = car_purchase_id;
        this.contract_id = contract_id;
        this.purchase_price = purchase_price;
    }

    public int getCar_purchase_id() {
        return this.car_purchase_id;
    }

    public int getContract_id() {
        return this.contract_id;
    }

    public double getPurchase_price() {
        return this.purchase_price;
    }

    public void setCar_purchase_id(int car_purchase_id) {
        this.car_purchase_id = car_purchase_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }
}
