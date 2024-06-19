package com.example.baselibs.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.baselibs.net.network.bean.DbRecord;
import com.example.baselibs.room.bean.PlanSportTargetByDay;

import java.util.List;

@Dao
public interface PlanSportTargetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(PlanSportTargetByDay... planSportTargetByDays);

    @Query("select * from planSportTarget where targetWhen = :runWhenToDay and phone = :phone")
    PlanSportTargetByDay queryByDate(String runWhenToDay, String phone);

    @Query("DELETE FROM planSportTarget where targetWhen = :runWhenToDay and phone = :phone")
    void delete(String runWhenToDay, String phone);
}
