package com.beyond.basic.b1_hello.controller;

public class Hello {
    private String name;
    private String email;

    public Hello(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
