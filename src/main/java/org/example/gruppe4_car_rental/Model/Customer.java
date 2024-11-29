package org.example.gruppe4_car_rental.Model;

public class Customer {
    private String cpr_number; 
    private String first_name;

    private String last_name;

    private String email;

    private String phone_number;

    private String address;
    private String zip_code;
private String city;

    //Tom konstruktør
    public Customer() {
    }

    //konstruktør med parametre
    public Customer(String cpr_number, String first_name, String last_name, String email, String phone_number, String address, String city, String zip_code) {
        this.cpr_number = cpr_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.zip_code = zip_code;
        this.city = city;
    }

    @Override
    public String toString() {
        return this.first_name +" "+ this.last_name;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCpr_number() {
        return cpr_number;
    }

    public void setCpr_number(String cpr_number) {
        this.cpr_number = cpr_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }
}
