package com.example.coolsports.bean;

public class Data {

    public Data() {
    }

    public Data(String dayOfMonth, String day, String dayAndMonth) {
        this.dayOfMonth = dayOfMonth;
        this.day = day;
        this.dayAndMonth = dayAndMonth;
    }

    private boolean isSelected = false;

    private String dayOfMonth = "";
    //周几
    private String day = "";
    //月份日期
    private String dayAndMonth = "";
    //yyyy-MM-dd格式日期
    private String dayTime = "";

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getDayAndMonth() {
        return dayAndMonth;
    }

    public void setDayAndMonth(String dayAndMonth) {
        this.dayAndMonth = dayAndMonth;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
