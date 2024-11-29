package org.example.gruppe4_car_rental.Model;

public class DamageReport {

    private int damage_report_id;

    private int contract_id;

    private double total_repair_price;

    public DamageReport(){

    }

    public DamageReport(int damage_report_id, int contract_id, double total_repair_price) {
        this.damage_report_id = damage_report_id;
        this.contract_id = contract_id;
        this.total_repair_price = total_repair_price;
    }

    public int getDamage_report_id() {
        return damage_report_id;
    }

    public int getContract_id() {
        return contract_id;
    }

    public double getTotal_repair_price() {
        return total_repair_price;
    }

    public void setDamage_report_id(int damage_report_id) {
        this.damage_report_id = damage_report_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public void setTotal_repair_price(double total_repair_price) {
        this.total_repair_price = total_repair_price;
    }
}



