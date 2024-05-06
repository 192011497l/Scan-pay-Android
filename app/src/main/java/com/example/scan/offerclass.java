package com.example.scan;

public class offerclass {

    String offer, expireDate, details;

    public String getoffer() {
        return offer;
    }

    public void setoffer(String offer) {
        this.offer = offer;
    }

    public String getexpireDate() {
        return expireDate;
    }

    public void setexpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getdetails() {
        return details;
    }

    public void setdetails(String details) {
        this.details = details;
    }



    public offerclass(String offer, String expireDate, String details) {
        this.offer = offer;
        this.expireDate = expireDate;
        this.details = details;

    }

    public offerclass() {
    }
}
