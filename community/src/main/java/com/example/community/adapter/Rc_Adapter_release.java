package com.example.community.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.community.R;
import com.example.community.bean.ImageViewInfo;
import com.previewlibrary.GPreviewBuilder;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

public class Rc_Adapter_release extends RecyclerView.Adapter<Rc_Adapter_release.ViewHolder> {

    private List<String> mList;
    private List<ImageViewInfo> list;
    private Context context;
    private Activity activity;


    public Rc_Adapter_release(Activity activity, List<String> list) {
        this.activity = activity;
        mList = list;
        context = activity.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < mList.size()) {
            Glide.with(context).load(mList.get(position)).into(holder.im);
            int n = position;
            holder.im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list = new ArrayList<>();
                    for (String s : mList) {
                        list.add(new ImageViewInfo(s));
                    }
                    if(holder.isVisibility) {
                        holder.bt_delete.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "onClick11: ");
                        holder.isVisibility = false;
                    }else {
                        //图片预览 关键
                        GPreviewBuilder.from(activity)
                                .setData(list)  //数据
                                .setCurrentIndex(n)  //图片下标
                                .setSingleFling(true)  //是否在黑屏区域点击返回
                                .setDrag(false)  //是否禁用图片拖拽返回
                                .setType(GPreviewBuilder.IndicatorType.Number)  //指示器类型
                                .start();  //启动
                    }
                }
            });

            holder.im.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    holder.bt_delete.setVisibility(View.VISIBLE);
                    holder.bt_delete.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onClick(View view) {
                            mList.remove(mList.get(n));
                            notifyDataSetChanged();
                            holder.bt_delete.setVisibility(View.INVISIBLE);
                        }
                    });
                    holder.isVisibility = true;
                    return true;
                }
            });

        } else {
            holder.im.setImageResource(R.drawable.community_p_2);
            holder.im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Matisse.from(activity)
                            .choose(MimeType.ofImage())
                            .countable(true)
                            .maxSelectable(9 - mList.size())
                            .capture(true)
                            .captureStrategy(new CaptureStrategy(true, "com.example.community.fileprovider"))
                            //.addFilter(new (320, 320, 5 * Filter.K * Filter.K))
                            .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.album_item_height))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .showPreview(true) // Default is `true`
                            .forResult(23);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mList.size() == 9) {
            return mList.size();
        }else {
            return mList.size() + 1;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private boolean isVisibility = false;
        private Button bt_delete;
        private ImageView im;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            im = itemView.findViewById(R.id.im_1);
            bt_delete = itemView.findViewById(R.id.bt_delete);
        }
    }

}
