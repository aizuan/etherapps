package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Adapter{

     String idnum;
     String item;
     String price;

    public Adapter() {
    }

    public Adapter(String idnum, String item , String price){
        this.idnum = idnum;
        this.item = item;
        this.price = price;
    }

    public String getIdnum() {
        return idnum;
    }
    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
}
