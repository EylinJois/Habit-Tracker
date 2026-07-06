package com.aenempeh.habittracker.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg habit: Habit)

    @Update
    fun updateHabit(habit:Habit)

    @Query("SELECT * FROM habit")
    fun selectAllHabit(): List<Habit>

    @Query("SELECT * FROM habit WHERE id= :id")
    fun selectHabit(id:Int): Habit

    @Delete
    fun deleteHabit(habit:Habit)
}

