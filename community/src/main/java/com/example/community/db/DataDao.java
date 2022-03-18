package com.example.community.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.community.bean.Data_rc;

import java.util.List;
import java.util.Observable;

import io.reactivex.Flowable;

@Dao
public interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Data_rc data_rc);

    @Query("SELECT * FROM community_data")
    List<Data_rc> loadAll();

    @Query("DELETE FROM community_data")
    void deleteAll();
}
