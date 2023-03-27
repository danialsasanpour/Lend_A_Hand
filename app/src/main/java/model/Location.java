package model;

import java.util.UUID;

public class Location {

    private String locationId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private double latitude;
    private double longitude;

    public Location(String street, String city, String state, String postalCode, String country, double latitude, double longitude) {
        this.locationId = UUID.randomUUID().toString();
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
        this.locationId = UUID.randomUUID().toString();
    }

    public Location(String street, String city, String state, String postalCode, String country) {
        this.locationId = UUID.randomUUID().toString();
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Location(double latitude, double longitude) {
        this.locationId = UUID.randomUUID().toString();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String displayLocation() {
        return street + ", " + city + ", " + state + ", " + postalCode + ".";
    }
}
