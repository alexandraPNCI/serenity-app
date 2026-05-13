package com.example.serenity.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.serenity.data.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}
