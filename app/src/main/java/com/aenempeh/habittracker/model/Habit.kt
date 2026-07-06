package com.aenempeh.habittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name="name")
    var name: String,
    @ColumnInfo(name="desc")
    var description: String,
    @ColumnInfo(name="goal")
    var goal: Int,
    @ColumnInfo(name="unit")
    var unit: String,
    @ColumnInfo(name="icon")
    var icon: String,
    @ColumnInfo(name="count")
    var currentCount: Int = 0
) {
    fun isCompleted() = currentCount >= goal
    fun progressPercent() = if (goal > 0)
        (currentCount.toFloat() / goal * 100).toInt().coerceAtMost(100)
    else 0
}