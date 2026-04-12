package com.example.jpa.interview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(name = "addr_street")
    private String street;

    @Column(name = "addr_city")
    private String city;

    @Column(name = "postal_code")
    private String zip;

    protected Address() {
    }

    public Address(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }
}
