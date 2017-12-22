package com.example.chinmoydash.farmersupporttech.firebasedata;


public class News {

    public String name, date, message;

    public News(String name, String message, String date) {

        this.name = name;
        this.message = message;
        this.date = date;
    }

    public News() {
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

    public String getDate() {
        return message;
    }

    public void setDate(String message) {
        this.message = message;
    }

}

