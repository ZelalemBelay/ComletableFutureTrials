package com.adeytech;

/**
 * Created by ZeeloGet on 6/23/2017.
 */
public class Address
{
    String address;
    String zip;
    String City;
    String state;

    public Address(String address, String zip, String city, String state) {
        this.address = address;
        this.zip = zip;
        City = city;
        this.state = state;
    }

    public Address() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
