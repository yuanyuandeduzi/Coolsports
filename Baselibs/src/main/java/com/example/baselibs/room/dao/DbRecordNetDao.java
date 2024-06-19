package com.example.baselibs.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.baselibs.net.network.bean.DbRecord;

import java.util.List;

@Dao
public interface DbRecordNetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(DbRecord... records);

    @Query("select * from dbRecord where phone = :phone")
    List<DbRecord> queryByPhone(String phone);

    @Query("select * from dbRecord where runWhenToDay = :runWhenToDay and phone = :phone")
    List<DbRecord> queryByDate(String runWhenToDay, String phone);

}
