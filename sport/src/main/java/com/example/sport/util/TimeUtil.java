package com.example.sport.util;

import android.os.SystemClock;
import android.widget.Chronometer;

public class TimeUtil {

    //计时器时间格式转换
    public static String timeToString(Chronometer ch) {
        long time = SystemClock.elapsedRealtime() - ch.getBase();
        int h = (int) (time / 3600000);
        int m = (int) (time - h * 3600000) / 60000;
        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
        String hh = h < 10 ? "0" + h : h + "";
        String mm = m < 10 ? "0" + m : m + "";
        String ss = s < 10 ? "0" + s : s + "";
        return hh + ":" + mm + ":" + ss;
    }

    //字符串获取分钟
    public static double stringToTime(String s) {
        String[] split = s.split(":");
        return Double.parseDouble(split[0]) * 60L + Double.parseDouble(split[1]) + Double.parseDouble(split[2]) / 60L;
    }

    //用于速度的时间转换
    public static String timeFormat(double time) {
        String result = "";
        int m = (int) time;
        int s = (int) ((time - m) * 60);
        if(s < 10) {
            result += m + "′0" + s + "″";
        }else {
            result += m + "′" + s + "″";
        }
        return result;
    }
}
