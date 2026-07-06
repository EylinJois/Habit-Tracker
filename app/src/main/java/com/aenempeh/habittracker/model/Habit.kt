package com.aenempeh.habittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name="name")
    val name: String,
    @ColumnInfo(name="desc")
    val description: String,
    @ColumnInfo(name="goal")
    val goal: Int,
    @ColumnInfo(name="unit")
    val unit: String,
    @ColumnInfo(name="icon")
    val icon: String,
    @ColumnInfo(name="count")
    var currentCount: Int = 0
) {
    fun isCompleted() = currentCount >= goal
    fun progressPercent() = if (goal > 0)
        (currentCount.toFloat() / goal * 100).toInt().coerceAtMost(100)
    else 0
}