package com.example.chinmoydash.farmersupporttech.firebasedata;


public class CropData {

    int currval, lastval;

    public CropData() {

    }

    public CropData(int currval, int lastval) {
        this.currval = currval;
        this.lastval = lastval;
    }

    public int getCurrval() {
        return currval;
    }

    public void setCurrval(int currval) {
        this.currval = currval;
    }

    public int getLastval() {
        return lastval;
    }

    public void setLastval(int lastval) {
        this.lastval = lastval;
    }
}
