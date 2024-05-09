package com.example.sport.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.sport.record.TargetDistance;

import java.util.List;

@Dao
public interface DbTargetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(TargetDistance... targetDistances);

    @Query("select * from target_distance where phone = :phone")
    TargetDistance query(String phone);

}
