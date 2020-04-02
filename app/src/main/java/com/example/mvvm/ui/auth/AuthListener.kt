package com.example.mvvm.ui.auth

import com.example.mvvm.data.db.entities.User

interface AuthListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message: String)
}