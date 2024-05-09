package com.example.sport.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.baselibs.net.network.bean.DbRecord;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DbRecordDao {

    @Insert
    void insert(DbRecord dbRecord);

    @Query("SELECT * FROM dbRecord")
    Flowable<List<DbRecord>> loadAll();

    @Delete
    void delete(DbRecord... dbRecords);

    @Query("DELETE FROM dbRecord")
    void deleteAll();

}
