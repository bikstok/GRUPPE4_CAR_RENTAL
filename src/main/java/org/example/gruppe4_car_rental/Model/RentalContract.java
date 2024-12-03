package org.example.gruppe4_car_rental.Model;

import java.sql.Date;

public class RentalContract {
    private int contract_id;
    private String cpr_number;
    private String frame_number;
    private Date start_date;
    private Date end_date;
    private boolean insurance;
    private double total_price;
    private int max_km;
    private boolean voucher;

    //tom konstruktør
    public RentalContract() {

    }

    //konstruktør med parametre
    public RentalContract(int contract_id, String cpr_number, String frame_number, Date start_date, Date end_date, boolean insurance, double total_price, int max_km, boolean voucher) {
        this.contract_id = contract_id;
        this.cpr_number = cpr_number;
        this.frame_number = frame_number;
        this.start_date = start_date;
        this.end_date = end_date;
        this.insurance = insurance;
        this.total_price = total_price;
        this.max_km = max_km;
        this.voucher = voucher;
    }

    public int getContract_id() {
        return this.contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public String getCpr_number() {
        return cpr_number;
    }

    public void setCpr_number(String cpr_number) {
        this.cpr_number = cpr_number;
    }

    public String getFrame_number() {
        return frame_number;
    }

    public void setFrame_number(String frame_number) {
        this.frame_number = frame_number;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getMax_km() {
        return max_km;
    }

    public void setMax_km(int max_km) {
        this.max_km = max_km;
    }

    public boolean getVoucher() {
        return voucher;
    }

    public void setVoucher(boolean voucher) {
        this.voucher = voucher;
    }
}
