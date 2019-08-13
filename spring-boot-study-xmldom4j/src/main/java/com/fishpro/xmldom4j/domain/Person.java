package com.fishpro.xmldom4j.domain;

public class Person {
    private String firstname;
    private String lastname;
    private PhoneNumber phone;
    private PhoneNumber fax;
    public Person(){

    }
    public Person(String firstname,String lastname){
        this.firstname=firstname;
        this.lastname=lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public void setPhone(PhoneNumber phone) {
        this.phone = phone;
    }

    public PhoneNumber getFax() {
        return fax;
    }

    public void setFax(PhoneNumber fax) {
        this.fax = fax;
    }
}

