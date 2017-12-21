package com.bot.users;

import com.bot.enums.City;

public class User {
    private City city;
    private boolean isCalcOn;

    public User(City city) {
        this.city = city;
        this.isCalcOn = false;
    }

    public City getCity() {
        return city;
    }

    public boolean isCalcOn() {
        return isCalcOn;
    }

    public void setCalcOn(boolean calcOn) {
        isCalcOn = calcOn;
    }
}
