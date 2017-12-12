package com.bot.currencies;

import java.text.Collator;
import java.util.Locale;

public class SimpleCurrency implements Comparable{
    private String name;

    private String getName() {
        return name;
    }

    private float rate;
    private String mark;

    public SimpleCurrency(String name, float rate, String mark) {
        this.name = name;
        this.rate = rate;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return String.format("%s %.3f", name, rate);
    }

    public String getMark() {
        return mark;
    }

    public float getRate() {
        return rate;
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareTo(((SimpleCurrency)o).getName());
    }
}
