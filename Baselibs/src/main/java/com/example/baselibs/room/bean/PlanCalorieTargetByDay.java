package com.example.baselibs.room.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "planCalorieTarget")
public class PlanCalorieTargetByDay {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String phone = "";
    private String targetWhen = "";
    private int target = 1500;
    private int nowInput = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTargetWhen() {
        return targetWhen;
    }

    public void setTargetWhen(String targetWhen) {
        this.targetWhen = targetWhen;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getNowInput() {
        return nowInput;
    }

    public void setNowInput(int nowInput) {
        this.nowInput = nowInput;
    }
}
