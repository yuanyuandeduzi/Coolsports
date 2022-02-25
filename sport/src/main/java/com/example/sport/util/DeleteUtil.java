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
    private boolean isCheck = false;
    private Map<Location_Adapter_Rc.ViewHolder, DbRecord> map = new HashMap<>();

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

    public void addView(Location_Adapter_Rc.ViewHolder viewHolder, DbRecord dbRecord) {
        map.put(viewHolder, dbRecord);
    }

    public void setVisibility() {
        for (Location_Adapter_Rc.ViewHolder viewHolder : map.keySet()) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }
        listener.isVisibility();
    }

    public void setInVisibility() {
        for (Location_Adapter_Rc.ViewHolder viewHolder : map.keySet()) {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.checkBox.setChecked(false);
        }
        listener.isUnVisibility();
        isCheck = false;
    }

    public void setCheckAll(boolean bool) {
        for (Location_Adapter_Rc.ViewHolder viewHolder : map.keySet()) {
            viewHolder.checkBox.setChecked(bool);
        }
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

    //给外面接口
    private DeleteListener listener;
    public void setListener(DeleteListener listener) {
        this.listener = listener;
    }

    public interface DeleteListener {
        void isVisibility();
        void isUnVisibility();
    }
}
