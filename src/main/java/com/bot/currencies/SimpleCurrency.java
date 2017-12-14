package com.bot.currencies;

public class SimpleCurrency implements Comparable{
    private String name;
    private static final String format = "%s  %.3f";

    private float rate;
    private String mark;

    public SimpleCurrency(String name, float rate, String mark) {
        this.name = name;
        this.rate = rate;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return String.format(format, name, rate);
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareTo(((SimpleCurrency)o).getName());
    }

    private String getName() {
        return name;
    }

    public String getMark() {
        return mark;
    }

    public float getRate() {
        return rate;
    }
}
