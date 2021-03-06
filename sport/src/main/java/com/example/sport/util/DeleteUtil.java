package com.example.sport.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.example.sport.adapter.Location_Adapter_Rc;
import com.example.sport.db.DbManger;
import com.example.sport.db.DbRecord;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class DeleteUtil {

    private static DeleteUtil sInstance;
    //判断是否为全选
    private boolean isCheck = false;
    private Map<Location_Adapter_Rc.ViewHolder, DbRecord> map = new HashMap<>();
    private int len = 0;
    //判断是不是处于选择阶段
    private boolean isDelete = false;

    public static DeleteUtil getInstance() {
        if (sInstance == null) {
            synchronized (DeleteUtil.class) {
                if (sInstance == null) {
                    sInstance = new DeleteUtil();
                }
            }
        }
        return sInstance;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public void addView(Location_Adapter_Rc.ViewHolder viewHolder, DbRecord dbRecord) {
        map.put(viewHolder, dbRecord);
    }

    public void setMapClear() {
        map.clear();
    }

    public void setVisibility() {
        for (Location_Adapter_Rc.ViewHolder viewHolder : map.keySet()) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }
        listener.isVisibility();
        isDelete = true;
    }

    public void setInVisibility() {
        for (Location_Adapter_Rc.ViewHolder viewHolder : map.keySet()) {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.checkBox.setChecked(false);
        }
        listener.isUnVisibility();
        isCheck = false;
        len = 0;
        isDelete = false;
    }

    public void setCheckAll(boolean bool) {
        for (Location_Adapter_Rc.ViewHolder viewHolder : map.keySet()) {
            if(!viewHolder.checkBox.isChecked() && bool) {
                len++;
            }else if (viewHolder.checkBox.isChecked() && !bool){
                len--;
            }
            viewHolder.checkBox.setChecked(bool);
        }
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean bool) {
        isCheck = bool;
    }

    @SuppressLint("CheckResult")
    public void deleteChecked(Context context) {
        for (Location_Adapter_Rc.ViewHolder viewHolder : map.keySet()) {
            if(viewHolder.checkBox.isChecked()) {
                DbManger.getInstance(context).delete(map.get(viewHolder)).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        map.remove(viewHolder);
                    }
                });
            }
        }
    }

    public Map<Location_Adapter_Rc.ViewHolder, DbRecord> getMap() {
        return map;
    }

    public void setImageView(int n, boolean bool) {
        listener.setImageView(n,bool);
    }

    //给外面接口
    private DeleteListener listener;
    public void setListener(DeleteListener listener) {
        this.listener = listener;
    }

    public interface DeleteListener {
        void isVisibility();
        void isUnVisibility();
        void setImageView(int n, boolean bool);
    }
}
