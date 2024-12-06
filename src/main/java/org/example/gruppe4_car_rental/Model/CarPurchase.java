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
        return car_purchase_id;
    }

    public int getContract_id() {
        return contract_id;
    }

    public double getPurchase_price() {
        return purchase_price;
    }
}
