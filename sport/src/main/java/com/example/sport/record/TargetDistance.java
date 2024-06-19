package com.example.sport.record;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "target_distance")
public class TargetDistance {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String phone;
    private Double targetDistance;

    public TargetDistance(Double targetDistance, String phone) {
        this.targetDistance = targetDistance;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(Double targetDistance) {
        this.targetDistance = targetDistance;
    }
}
