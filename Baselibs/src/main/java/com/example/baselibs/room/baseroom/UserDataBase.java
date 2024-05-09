package com.example.baselibs.room.baseroom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.baselibs.net.network.bean.User;
import com.example.baselibs.room.dao.UserDao;

@Database(entities = {User.class}, version = 1, exportSchema = true)
public abstract class UserDataBase extends RoomDatabase {
    public static UserDataBase sInstance;
    private static final String databaseName = "data";

    public static UserDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (UserDataBase.class) {
                if (sInstance == null) {
                    //三个参数：第一个Context对象，第二个为数据库类，第三个为数据库名字
                    sInstance = Room.databaseBuilder(context, UserDataBase.class, databaseName)
                            // 设置是否允许在主线程做查询操作
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract UserDao getUserDao();

}
