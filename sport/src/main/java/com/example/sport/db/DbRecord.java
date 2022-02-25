package com.example.sport.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dbRecord")
public class DbRecord {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String distance = "";
    private String runTime = "";
    private String runWhen = "";

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
        this.runWhen = runWhen;
    }
}
