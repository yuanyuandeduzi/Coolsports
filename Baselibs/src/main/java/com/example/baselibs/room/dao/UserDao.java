package com.example.baselibs.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baselibs.net.network.bean.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(User... users);

    @Query("select * from user where phone = :phone and password = :password")
    User query(String phone, String password);

    @Query("select * from user where phone = :phone")
    User query(String phone);

    @Update
    void update(User... users);
}
