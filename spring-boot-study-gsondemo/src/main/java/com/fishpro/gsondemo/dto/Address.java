package com.fishpro.gsondemo.dto;

public class Address {
    private String street;
    private String zipcode;
    private String mobile;

    public Address(){

    }

    public Address(String street,String zipcode,String mobile){
        this.street=street;
        this.zipcode=zipcode;
        this.mobile=mobile;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}