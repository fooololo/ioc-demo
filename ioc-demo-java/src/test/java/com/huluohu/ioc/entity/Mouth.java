package com.huluohu.ioc.entity;

public class Mouth {
    private String name;

    public Mouth(String name) {
        this.name = name;
    }

    public void speek(){
        System.out.println("say hello " + name);
    }
}
