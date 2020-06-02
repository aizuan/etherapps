package com.example.myapplication;

import android.app.Application;

public class GlobalVariable extends Application {
    private String Address1;     //要傳送的字串
    //修改 變數字串
    public void setWord(String word) {
        this.Address1 = word;
    }
        public String getWord() {
            return Address1;
        }
}
