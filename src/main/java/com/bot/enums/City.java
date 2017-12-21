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
    SUMY("sumy/"),
    CHERNIGOV("chernigov/"),
    ZHITOMIR("zhitomir/"),
    DEFAULT("all/");

    private String endOfUrl;

    City(String endOfUrl) {
        this.endOfUrl = endOfUrl;
    }

    public String getEndOfUrl() {
        return endOfUrl;
    }

    public static City getCity(String message) {
        switch (message) {
            case "Киев" : return City.KIEV;
            case "Одесса": return City.ODESSA;
            case "Львов": return  City.LVOV;
            case "Харьков": return City.KHARKOV;
            case "Днепр": return City.DNIPRO;
            case "Запорожье": return City.ZAPOROZHYE;
            case "Николаев": return City.NIKOLAEV;
            case "Донецк": return City.DONETSK;
            case "Винница" : return City.VINNITSA;
            case "Мариуполь" : return City.MARIUPOL;
            case "Полтава" : return City.POLTAVA;
            case "Хмельницкий" : return City.KHMENTISKIY;
            case "Сумы": return City.SUMY;
            case "Чернигов": return City.CHERNIGOV;
            case "Житомир": return City.ZHITOMIR;
            default: return City.DEFAULT;
        }
    }
}
