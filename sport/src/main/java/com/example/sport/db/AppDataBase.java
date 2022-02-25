package com.example.sport.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DbRecord.class}, version = 1, exportSchema = true)
public abstract class AppDataBase extends RoomDatabase{

    public static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDataBase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context,AppDataBase.class,"data").build();
                }
            }
        }
        return sInstance;
    }

    public abstract DbRecordDao getDao();

}
