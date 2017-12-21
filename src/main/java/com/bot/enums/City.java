package com.bot.enums;

public enum City {
    KIEV("kiev/", "Киев"),
    ODESSA("odessa/", "Одеccа"),
    LVOV("lvov/", "Львов"),
    KHARKOV("kharkov/", "Харьков"),
    DNIPRO("dnepropetrovsk/", "Днепр"),
    ZAPOROZHYE("zaporozhye/", "Запорожье"),
    NIKOLAEV("nikolaev/", "Hиколаев"),
    DONETSK("donetsk/", "Донецк"),
    VINNITSA("vinnitsa/", "Винница"),
    MARIUPOL("mariupol/", "Мариуполь"),
    POLTAVA("poltava/", "Полтава"),
    KHMENTISKIY("khmelnitskiy/", "Хмельницкий"),
    SUMY("sumy/", "Сумы"),
    CHERNIGOV("chernigov/", "Чернигов"),
    ZHITOMIR("zhitomir/", "Житомир"),
    DEFAULT("all/", "cредний по Украине");

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
        for (City city: City.values()) {
            if (message.equals(city.getName())) return city;
        }
        return City.DEFAULT;
    }

    public String getName() {
        try {
            return new String(name.getBytes(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
