package com.example.coolsports.bean;

public class DishesData {

    public DishesData() {
    }

    public DishesData(String dishes_Name, int dishes_calorie) {
        this.dishes_Name = dishes_Name;
        this.dishes_calorie = dishes_calorie;
    }

    private String dishes_Name;
    private int dishes_calorie;

    public String getDishes_Name() {
        return dishes_Name;
    }

    public void setDishes_Name(String dishes_Name) {
        this.dishes_Name = dishes_Name;
    }

    public int getDishes_calorie() {
        return dishes_calorie;
    }

    public void setDishes_calorie(int dishes_calorie) {
        this.dishes_calorie = dishes_calorie;
    }
}
