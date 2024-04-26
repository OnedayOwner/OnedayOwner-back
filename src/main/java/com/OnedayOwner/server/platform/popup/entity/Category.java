package com.OnedayOwner.server.platform.popup.entity;


public enum Category {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    FUSION("퓨전"),
    ETC("기타"),

    ;

    private String name;

    Category(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
