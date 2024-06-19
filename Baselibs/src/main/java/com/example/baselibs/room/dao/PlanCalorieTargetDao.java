package com.example.baselibs.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.baselibs.room.bean.PlanCalorieTargetByDay;
import com.example.baselibs.room.bean.PlanSportTargetByDay;

import java.util.List;

@Dao
public interface PlanCalorieTargetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(PlanCalorieTargetByDay... planCalorieTargetByDays);

    @Query("select * from planCalorieTarget where targetWhen = :runWhenToDay and phone = :phone")
    PlanCalorieTargetByDay queryByDate(String runWhenToDay, String phone);

    @Query("DELETE FROM planCalorieTarget where targetWhen = :runWhenToDay and phone = :phone")
    void delete(String runWhenToDay, String phone);
}
