package com.bot.enums;

public enum City {
    KIEV("kiev/"),
    ODESSA("odessa/"),
    LVOV("lvov/"),
    KHARKOV("kharkov/"),
    DNIPRO("dnepropetrovsk/"),
    ZAPOROZHYE("zaporozhye/"),
    NIKOLAEV("nikolaev/"),
    DONETSK("donetsk/"),
    VINNITSA("vinnitsa/"),
    MARIUPOL("mariupol/"),
    POLTAVA("poltava/"),
    KHMENTISKIY("khmelnitskiy/"),
    SUMI("sumy/"),
    CHERNIGOV("chernigov/");

    private String endOfUrl;

    City(String endOfUrl) {
        this.endOfUrl = endOfUrl;
    }

    public String getEndOfUrl() {
        return endOfUrl;
    }
}
