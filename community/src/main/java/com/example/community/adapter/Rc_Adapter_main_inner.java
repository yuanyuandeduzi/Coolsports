package com.example.community.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.community.R;
import com.example.community.bean.ImageViewInfo;
import com.previewlibrary.GPreviewBuilder;

import java.util.ArrayList;
import java.util.List;

public class Rc_Adapter_main_inner extends RecyclerView.Adapter<Rc_Adapter_main_inner.ViewHolder> {

    private List<String> mList;
    private Context context;
    private List<ImageViewInfo> list;

    public Rc_Adapter_main_inner(List<String> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_3, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(mList.get(position)).into(holder.im);
        int n = position;
        holder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list = new ArrayList<>();
                for (String s : mList) {
                    list.add(new ImageViewInfo(s));
                }
                //图片预览（关键
                GPreviewBuilder.from((Activity) context)
                        .setData(list)  //数据
                        .setCurrentIndex(n)  //图片下标
                        .setSingleFling(true)  //是否在黑屏区域点击返回
                        .setDrag(false)  //是否禁用图片拖拽返回
                        .setType(GPreviewBuilder.IndicatorType.Number)  //指示器类型
                        .start();  //启动
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView im;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            im = itemView.findViewById(R.id.im_1);
        }
    }
}
