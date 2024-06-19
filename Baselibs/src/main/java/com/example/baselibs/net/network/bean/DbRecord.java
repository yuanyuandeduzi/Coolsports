package com.example.baselibs.net.network.bean;

import android.icu.util.TimeUnit;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.baselibs.TimeUtil;

import java.util.Date;

@Entity(tableName = "dbRecord")
public class DbRecord {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    public DbRecord() {
    }

    private String phone = "";
    private String distance = "";
    private String runTime = "";
    private String runWhen = "";
    private String runWhenToDay = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getRunWhen() {
        return runWhen;
    }

    public void setRunWhen(String runWhen) {
        this.runWhenToDay = runWhen.split(" ")[0];
        this.runWhen = runWhen;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRunWhenToDay() {
        return runWhenToDay;
    }

    public void setRunWhenToDay(String runWhenToDay) {
        this.runWhenToDay = runWhenToDay;
    }
}
