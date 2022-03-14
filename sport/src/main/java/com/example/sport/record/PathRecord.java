package com.example.sport.record;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PathRecord{

    //运动轨迹
    private List<LatLng> pathLinePoints = new ArrayList<>();
    //运动距离
    private float distance;
    //运动时长
    private float duration;
    //步幅
    private float stride = 75;
    //步数
    private int steps;

    public List<LatLng> getPathLinePoints() {
        return pathLinePoints;
    }

    public void setPathLinePoints(List<LatLng> pathLinePoints) {
        this.pathLinePoints = pathLinePoints;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getStride() {
        return stride;
    }

    public void setStride(float stride) {
        this.stride = stride;
    }

    public void addLaLng(LatLng latLng) {
        pathLinePoints.add(latLng);
    }

    public PathRecord() {
    }
}
