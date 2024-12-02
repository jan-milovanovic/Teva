package com.blankcat.teva.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.blankcat.teva.models.User

@Dao interface UserDao {
    @Query("SELECT * FROM users WHERE id == 0 LIMIT 1")
    suspend fun getUser(): User?

    @Query("SELECT * FROM users WHERE email == :email AND password == :password")
    suspend fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}