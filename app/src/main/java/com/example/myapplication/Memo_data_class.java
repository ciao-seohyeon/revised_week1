package com.example.myapplication;

public class Memo_data_class {
    private String titleStr;
    private String date;

    public void setTitle(String title){
        titleStr = title;
    }

    public void setDate(String _date){
        date = _date;
    }

    public String getTitle(){
        return this.titleStr;
    }

    public String getDate(){
        return this.date;
    }
}
