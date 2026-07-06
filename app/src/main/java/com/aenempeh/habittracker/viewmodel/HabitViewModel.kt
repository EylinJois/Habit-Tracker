package com.aenempeh.habittracker.viewmodel

import androidx.lifecycle.MutableLiveData
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.aenempeh.habittracker.model.Habit
import com.aenempeh.habittracker.model.HabitDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class HabitViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    val habitsLD = MutableLiveData<List<Habit>>()
    val isSuccessLD = MutableLiveData<Boolean>()

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun createHabit(name: String, desc: String, goal: Int, unit: String, icon: String) {
        try {
            val newHabit = Habit(
                name = name,
                description = desc,
                goal = goal,
                unit = unit,
                icon = icon)

            launch {
                val db = HabitDatabase.buildDatabase(getApplication())
                db.habitDao().insertAll(newHabit)
                isSuccessLD.postValue(true)
                loadHabits()
            }
        } catch (e: Exception) {
            Log.e("HabitViewModel", "Failed to add habit", e)
            isSuccessLD.postValue(false)
        }
    }

    fun updateHabit(habit: Habit) {
        launch {
            try {
                val db = HabitDatabase.buildDatabase(getApplication())
                db.habitDao().updateHabit(habit)
                isSuccessLD.postValue(true)
                loadHabits()
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Update failed", e)
                isSuccessLD.postValue(false)
            }
        }
    }

    fun loadHabits() {
        launch {
            val db = HabitDatabase.buildDatabase(getApplication())
            val habits = db.habitDao().selectAllHabit()
            habitsLD.postValue(habits)
        }
    }

    fun incrementProgress(habit: Habit) {
        if (habit.currentCount < habit.goal) {
            habit.currentCount++
            updateHabit(habit)
        }
    }

    fun decrementProgress(habit: Habit) {
        if (habit.currentCount > 0) {
            habit.currentCount--
            updateHabit(habit)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
