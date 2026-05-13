package com.example.serenity.data.repository

import com.example.serenity.data.database.UserDao
import com.example.serenity.data.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun register(username: String, passwordHash: String) {
        val existing = userDao.getUserByUsername(username)
        if (existing == null) {
            userDao.insertUser(
                User(username = username, passwordHash = passwordHash)
            )
        }
    }

    suspend fun getUser(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}
