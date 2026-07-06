package com.aenempeh.habittracker.viewmodel

import androidx.lifecycle.MutableLiveData
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.aenempeh.habittracker.model.Habit
import com.aenempeh.habittracker.model.HabitDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class HabitViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val prefs = application.getSharedPreferences("habits_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    val habitsLD = MutableLiveData<List<Habit>>()

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
                val db = HabitDatabase.buildDatabase(
                    getApplication()
                )
                db.habitDao().insertAll(newHabit)
            }
        } catch (e: Exception) {
            Log.e("HabitViewModel", "Failed to add habit", e)
        }
    }

    fun updateHabit(habit: Habit) {
        launch {
            try {
                val db = HabitDatabase.buildDatabase(
                    getApplication()
                )
                db.habitDao().updateHabit(habit)
                val updatedList = db.habitDao().selectAllHabit()
                habitsLD.postValue(updatedList)
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Update failed", e)
            }
        }
    }

    fun getHabits(): List<Habit> {
        val json = prefs.getString("habits_list", null) ?: return emptyList()
        val type = object : TypeToken<List<Habit>>() {}.type
        return gson.fromJson(json, type)
    }

    fun loadHabits(){
        habitsLD.value = ArrayList(getHabits())
    }

    fun incrementProgress(habitId: Int){
        val list = getHabits().toMutableList()
        val h = list.find { it.id == habitId} ?: return
        if (h.currentCount < h.goal){
            h.currentCount++
            prefs.edit().putString("habits_list", gson.toJson(list)).apply()
            habitsLD.value = ArrayList(list)
        }
    }

    fun decrementProgress(habitId: Int) {
        val list = getHabits().toMutableList()
        val h = list.find { it.id == habitId } ?: return
        if (h.currentCount > 0) {
            h.currentCount--
            prefs.edit().putString("habits_list", gson.toJson(list)).apply()
            habitsLD.value = ArrayList(list)
        }
    }
}