package com.example.coolsports.util;

import android.animation.ValueAnimator;
import android.widget.TextView;

import com.example.coolsports.R;
import com.example.coolsports.adapter.Plan_Fragment_Adapter_Rc1;
import com.example.coolsports.bean.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Plan_Fragment_RcUtils {

    private Map<Plan_Fragment_Adapter_Rc1.ViewHolder, Data> map = new HashMap<>();

    private static Plan_Fragment_RcUtils sInstance;

    //单例
    public static Plan_Fragment_RcUtils getsInstance() {
        if (sInstance == null) {
            synchronized (Plan_Fragment_RcUtils.class) {
                if (sInstance == null) {
                    sInstance = new Plan_Fragment_RcUtils();
                }
            }
        }
        return sInstance;
    }

    public void addViewHolder(Plan_Fragment_Adapter_Rc1.ViewHolder holder, Data data) {
        map.put(holder, data);
    }

    //界面显示部分的刷新
    public void setIsSelected(Plan_Fragment_Adapter_Rc1.ViewHolder holder, Data data, int position) {
        for (Plan_Fragment_Adapter_Rc1.ViewHolder viewHolder : map.keySet()) {
            viewHolder.tv_2.setTextSize(20);
            viewHolder.tv_2.setTextColor(holder.tv_2.getResources().getColor(R.color.color_day));
            Objects.requireNonNull(map.get(viewHolder)).setSelected(false);
        }
        if(holder.tv_2.getTextSize() != 82.5) {
            selectAnimation(20, 30, holder.tv_2);
        }
        holder.tv_2.setTextColor(holder.tv_2.getResources().getColor(R.color.black));
        listener.isNotToday(data,position);
        if(data.getDayAndMonth().equals("今日")) {
            listener.isInVisibility();
        }
    }

    public Map<Plan_Fragment_Adapter_Rc1.ViewHolder, Data> getMap() {
        return map;
    }

    private ChangeListener listener;

    public interface ChangeListener {
        void isInVisibility();
        void isNotToday(Data data, int position);
        void updateMainData(Data data);
    }

    public void updateDay(Data data) {
        listener.updateMainData(data);
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void selectAnimation(int start, int end, TextView view) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int size = (int) valueAnimator.getAnimatedValue();
                view.setTextSize(size);
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

}
