package com.example.baselibs.room.baseroom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.baselibs.net.network.bean.DbRecord;
import com.example.baselibs.room.bean.PlanCalorieTargetByDay;
import com.example.baselibs.room.bean.PlanSportTargetByDay;
import com.example.baselibs.room.dao.DbRecordNetDao;
import com.example.baselibs.room.dao.PlanCalorieTargetDao;
import com.example.baselibs.room.dao.PlanSportTargetDao;

@Database(entities = {DbRecord.class, PlanSportTargetByDay.class, PlanCalorieTargetByDay.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDataBase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, AppDataBase.class,"sportDataNet")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract DbRecordNetDao getDbRecordDao();

    public abstract PlanSportTargetDao getPlanSportTargetDao();

    public abstract PlanCalorieTargetDao getPlanCalorieTargetDao();
}
