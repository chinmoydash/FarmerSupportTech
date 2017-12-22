package com.example.chinmoydash.farmersupporttech.firebasedata;


public class Comments {

    public String name, message;

    public Comments(String name, String message) {

        this.name = name;
        this.message = message;
    }

    public Comments() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
