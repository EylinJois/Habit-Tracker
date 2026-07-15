package com.aenempeh.habittracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.aenempeh.habittracker.model.HabitDatabase
import com.aenempeh.habittracker.model.User
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val loginStatusLD = MutableLiveData<Boolean>()
    val loginErrorLD = MutableLiveData<String?>()
    val sessionUserLD = MutableLiveData<String?>()

    private val prefs = application.getSharedPreferences("login_session", Context.MODE_PRIVATE)

    init {
        // Pre-populate database with default user if empty
        launch {
            val db = HabitDatabase.buildDatabase(getApplication())
            val existingUser = db.userDao().getUser("student")
            if (existingUser == null) {
                db.userDao().insertUser(User("student", "123"))
            }
        }
    }

    fun login(username: String, pass: String) {
        launch {
            val db = HabitDatabase.buildDatabase(getApplication())
            val user = db.userDao().getUser(username)

            if (user != null && user.password == pass) {
                // Success
                prefs.edit().putString("username", username).apply()
                loginStatusLD.postValue(true)
                loginErrorLD.postValue(null)
            } else {
                // Failure
                loginStatusLD.postValue(false)
                if (user == null) {
                    loginErrorLD.postValue("Username salah")
                } else {
                    loginErrorLD.postValue("Password salah")
                }
            }
        }
    }

    fun checkSession() {
        val username = prefs.getString("username", null)
        sessionUserLD.value = username
    }

    fun logout() {
        prefs.edit().remove("username").apply()
        sessionUserLD.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
