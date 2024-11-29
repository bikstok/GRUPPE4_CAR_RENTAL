package org.example.gruppe4_car_rental.Model;

public class ZipCode {
    private String zip_code;
    private String city;

    public ZipCode(){

    }
    public ZipCode(String zip_code, String city) {
        this.zip_code = zip_code;
        this.city = city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getCity() {
        return city;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
