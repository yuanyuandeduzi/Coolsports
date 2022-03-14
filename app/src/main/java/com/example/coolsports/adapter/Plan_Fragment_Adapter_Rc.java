package com.example.coolsports.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coolsports.R;
import com.example.coolsports.bean.Data;
import com.example.coolsports.util.Plan_Fragment_RcUtils;

import java.util.List;

public class Plan_Fragment_Adapter_Rc extends RecyclerView.Adapter<Plan_Fragment_Adapter_Rc.ViewHolder> {

    private List<Data> mList;
    private Plan_Fragment_RcUtils planUtil;

    public Plan_Fragment_Adapter_Rc(List<Data> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_rc, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_1.setText(mList.get(position).getDay());
        holder.tv_2.setText(mList.get(position).getDayOfMonth());

        planUtil = Plan_Fragment_RcUtils.getsInstance();
        planUtil.addViewHolder(holder,mList.get(position));
        if(mList.get(position).getDayAndMonth().equals("今日")) {
            holder.tv_1.setTextColor(holder.tv_1.getResources().getColor(R.color.black));
        }else {
            holder.tv_1.setTextColor(holder.tv_1.getResources().getColor(R.color.color_day));
        }
        //保证新加载进来的为正确形式
        if(!mList.get(position).isSelected()) {
            holder.tv_2.setTextSize(20);
            holder.tv_2.setTextColor(holder.tv_2.getResources().getColor(R.color.color_day));
        }else {
            holder.tv_2.setTextSize(30);
            holder.tv_2.setTextColor(holder.tv_2.getResources().getColor(R.color.black));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Data data : mList) {
                    data.setSelected(false);
                }
                planUtil.setIsSelected(holder, mList.get(position));
                mList.get(position).setSelected(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_1;
        public TextView tv_2;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            view = itemView;
        }
    }
}
