package com.example.sport.record;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PathRecord implements Parcelable{
    //运动开始点
    private LatLng startPoint;
    //运动结束点
    private LatLng endPoint;
    //运动轨迹
    private List<LatLng> pathLinePoints = new ArrayList<>();
    //运动距离
    private double distance;
    //运动时长
    private long duration;
    //消耗卡路里
    private double calorie;
    //平均配速(分钟/公里)
    private double distribution;


    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }

    public List<LatLng> getPathLinePoints() {
        return pathLinePoints;
    }

    public void setPathLinePoints(List<LatLng> pathLinePoints) {
        this.pathLinePoints = pathLinePoints;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getDistribution() {
        return distribution;
    }

    public void setDistribution(double distribution) {
        this.distribution = distribution;
    }


    public void addLaLng(LatLng latLng) {
        pathLinePoints.add(latLng);
    }
    public PathRecord() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public PathRecord(Parcel in) {
        this.duration = in.readLong();
        this.distance = in.readDouble();
        this.calorie = in.readDouble();
        this.distribution = in.readDouble();
        in.readList(pathLinePoints, LatLng.class.getClassLoader());
        this.endPoint = (LatLng) in.readValue(LatLng.class.getClassLoader());
        this.startPoint = (LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.duration);
        parcel.writeDouble(this.distance);
        parcel.writeDouble(this.calorie);
        parcel.writeDouble(this.distribution);
        parcel.writeList(this.pathLinePoints);
        parcel.writeValue(this.endPoint);
        parcel.writeValue(this.startPoint);
    }

    public static final Creator<PathRecord> CREATOR = new Creator<PathRecord>() {
        @Override
        public PathRecord createFromParcel(Parcel parcel) {
            return new PathRecord(parcel);
        }

        @Override
        public PathRecord[] newArray(int i) {
            return new PathRecord[0];
        }
    };
}
