package com.example.sport.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.baselibs.net.network.bean.DbRecord;

@Database(entities = {DbRecord.class}, version = 1, exportSchema = false)
public abstract class AppDataBaseNet extends RoomDatabase {

    public static AppDataBaseNet sInstance;

    public static AppDataBaseNet getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDataBaseLocation.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, AppDataBaseNet.class,"sportDataNet")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract DbRecordNetDao getDao();
}
