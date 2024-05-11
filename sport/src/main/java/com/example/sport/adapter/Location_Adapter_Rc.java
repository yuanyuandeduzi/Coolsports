package com.example.sport.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baselibs.net.network.bean.DbRecord;
import com.example.baselibs.room.baseroom.AppDataBase;
import com.example.sport.R;
import com.example.sport.db.DbManger;
import com.example.sport.util.DeleteUtil;

import java.util.List;

import io.reactivex.functions.Consumer;

public class Location_Adapter_Rc extends RecyclerView.Adapter<Location_Adapter_Rc.ViewHolder> {

    private List<DbRecord> mList;
    private Context mContext;

    public Location_Adapter_Rc(List<DbRecord> mList, Context context) {
        this.mList = mList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recrview_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DbRecord dbRecord = mList.get(position);
        holder.tv_time.setText(dbRecord.getRunWhen());
        holder.tv_useTime.setText(dbRecord.getRunTime());
        holder.tv_distance.setText(dbRecord.getDistance());

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DeleteUtil.getInstance().setVisibility();
                DeleteUtil.getInstance().setLen(0);
                return true;
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DeleteUtil.getInstance().isDelete()) {
                    DeleteUtil.getInstance().setInVisibility();
                }else {
                    initDialog(holder.view, dbRecord,holder);
                }
            }
        });


        //选择
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()) {
                    DeleteUtil.getInstance().setLen(DeleteUtil.getInstance().getLen() + 1);
                }else {
                    DeleteUtil.getInstance().setLen(DeleteUtil.getInstance().getLen() - 1);
                }

                if(DeleteUtil.getInstance().getLen() == DeleteUtil.getInstance().getMap().keySet().size()) {
                    DeleteUtil.getInstance().setImageView(R.drawable.im_check_2,true);
                }else {
                    DeleteUtil.getInstance().setImageView(R.drawable.im_check_1,false);
                }
            }
        });
        DeleteUtil.getInstance().addView(holder, dbRecord);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_distance;
        private final TextView tv_useTime;
        private final TextView tv_time;
        public CheckBox checkBox;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_distance = itemView.findViewById(R.id.tv_distance_location);
            tv_useTime = itemView.findViewById(R.id.tv_useTime_location);
            tv_time = itemView.findViewById(R.id.tv_time_location);
            checkBox = itemView.findViewById(R.id.check_Location);
            view = itemView;
        }
    }

    //退出的dialog
    private void initDialog(View view, DbRecord dbRecord, ViewHolder holder) {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(view.getContext());
        alterDialog.setCancelable(false);
        alterDialog.setTitle("是否上传本条记录？");
        alterDialog.setNegativeButton("取消上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alterDialog.setPositiveButton("确定上传", new DialogInterface.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppDataBase.getInstance(mContext).getDbRecordDao().insert(dbRecord);

                DbManger.getInstance(view.getContext()).delete(dbRecord).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        DeleteUtil.getInstance().getMap().remove(holder);
                    }
                });
                /*Map<String, String> map = new HashMap<>();
                map.put("runTime", dbRecord.getRunTime());
                map.put("runWhen", dbRecord.getRunWhen());
                map.put("distance", dbRecord.getDistance());
                //Uid
                map.put("uid",UploadUtil.uid);
                ApiService postService = UploadUtil.sentPostService();
                postService.sport_postCall("run/addRunRecord", map).enqueue(new Callback<BaseResponse<String>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                        if (response.body() == null) {
                            Toast.makeText(view.getContext(), "上传失败1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().isSuccess()) {
                            Toast.makeText(view.getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            DbManger.getInstance(view.getContext()).delete(dbRecord).subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(Integer integer) throws Exception {
                                    DeleteUtil.getInstance().getMap().remove(holder);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                        Toast.makeText(view.getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
        alterDialog.show();
    }
}
