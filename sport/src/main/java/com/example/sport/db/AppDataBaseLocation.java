package com.example.sport.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.baselibs.net.network.bean.DbRecord;
import com.example.sport.record.TargetDistance;

@Database(entities = {DbRecord.class, TargetDistance.class}, version = 1, exportSchema = false)
public abstract class AppDataBaseLocation extends RoomDatabase{

    public static AppDataBaseLocation sInstance;

    public static AppDataBaseLocation getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDataBaseLocation.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, AppDataBaseLocation.class,"sportDataLocation")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract DbRecordDao getDbRecordDao();

    public abstract DbTargetDao getTargetDao();

}
