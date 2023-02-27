package model;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String location;
    private String profilePicture;
    private double radius;

    public User(String username, String name, String password, String email, String phoneNumber, String location, String profilePicture, double radius) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.profilePicture = profilePicture;
        this.radius = radius;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicture(){ return profilePicture;}

    public void setProfilePicture(String profilePicture){ this.profilePicture = profilePicture;}

    public double getRadius(){ return radius;}

    public void setRadius(float radius){ this.radius = radius;}

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location=" + location +
                '}';
    }
}
