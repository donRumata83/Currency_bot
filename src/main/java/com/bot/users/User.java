package com.bot.users;

import com.bot.enums.City;

public class User {
    private long id;
    private City city;
    private boolean isCalcOn;

    public User(long id, City city) {
        this.id = id;
        this.city = city;
        isCalcOn = false;
    }

    public User() {
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCalcOn() {
        return isCalcOn;
    }

    public void setCalcOn(boolean calcOn) {
        isCalcOn = calcOn;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", city=" + city +
                ", isCalcOn=" + isCalcOn +
                '}';
    }
}
