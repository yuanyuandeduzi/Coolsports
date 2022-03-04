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
    private String day = "";
    private String dayAndMonth = "";

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
