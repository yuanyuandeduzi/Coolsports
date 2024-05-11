package com.example.community.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.community.bean.Data_rc;

@Database(entities = {Data_rc.class},version = 1,exportSchema = false)
public abstract class CommunityDataBase  extends RoomDatabase {

    private static CommunityDataBase sInstance;

    public static CommunityDataBase getInstance(Context context) {
        if(sInstance == null) {
            synchronized (CommunityDataBase.class) {
                if(sInstance == null) {
                    sInstance = Room.databaseBuilder(context,CommunityDataBase.class,"data_community_upload").
                            allowMainThreadQueries().build();
                }
            }
        }
        return sInstance;
    }

    public abstract DataDao getDataDao();
}
