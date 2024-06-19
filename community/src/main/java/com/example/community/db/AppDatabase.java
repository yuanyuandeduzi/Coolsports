package com.example.community.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.community.bean.Data_rc;

@Database(entities = {Data_rc.class},version = 1,exportSchema = false)
public abstract  class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if(sInstance == null) {
            synchronized (AppDatabase.class) {
                if(sInstance == null) {
                    sInstance = Room.databaseBuilder(context,AppDatabase.class,"data_community").
                    allowMainThreadQueries().build();
                }
            }
        }
        return sInstance;
    }

    public abstract DataDao getDataDao();

}
