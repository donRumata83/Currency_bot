package com.bot.enums;

public enum City {
    KIEV("kiev/", "в городе Киев"),
    ODESSA("odessa/", "в городе Одесса"),
    LVOV("lvov/", "в городе Львов"),
    KHARKOV("kharkov/", "в городе Харьков"),
    DNIPRO("dnepropetrovsk/", "в городе Днепр"),
    ZAPOROZHYE("zaporozhye/", "в городе Запорожье"),
    NIKOLAEV("nikolaev/", "в городе Николаев"),
    DONETSK("donetsk/", "в городе Донецк"),
    VINNITSA("vinnitsa/", "в городе Винница"),
    MARIUPOL("mariupol/", "в городе Мариуполь"),
    POLTAVA("poltava/", "в городе Полтава"),
    KHMENTISKIY("khmelnitskiy/", "в городе Хмельницкий"),
    SUMY("sumy/", "в городе Сумы"),
    CHERNIGOV("chernigov/", "в городе Чернигов"),
    ZHITOMIR("zhitomir/", "в городе Житомир"),
    DEFAULT("all/", "средний по Украине");

    private String endOfUrl;
    private String name;

    City(String endOfUrl, String name) {
        this.endOfUrl = endOfUrl;
        this.name = name;
    }

    public String getEndOfUrl() {
        return endOfUrl;
    }

    public static City getCity(String message) {
        switch (message) {
            case "Киев":
                return City.KIEV;
            case "Одесса":
                return City.ODESSA;
            case "Львов":
                return City.LVOV;
            case "Харьков":
                return City.KHARKOV;
            case "Днепр":
                return City.DNIPRO;
            case "Запорожье":
                return City.ZAPOROZHYE;
            case "Николаев":
                return City.NIKOLAEV;
            case "Донецк":
                return City.DONETSK;
            case "Винница":
                return City.VINNITSA;
            case "Мариуполь":
                return City.MARIUPOL;
            case "Полтава":
                return City.POLTAVA;
            case "Хмельницкий":
                return City.KHMENTISKIY;
            case "Сумы":
                return City.SUMY;
            case "Чернигов":
                return City.CHERNIGOV;
            case "Житомир":
                return City.ZHITOMIR;
            default:
                return City.DEFAULT;
        }
    }

    public String getName() {
        return name;
    }
}
