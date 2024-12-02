package com.blankcat.teva.repository

import com.blankcat.teva.data.dao.UserDao
import com.blankcat.teva.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val userDao: UserDao) {

    private val _isAuthenticatedFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticatedFlow

    suspend fun init() {
        _isAuthenticatedFlow.value = userDao.getUser() != null
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            println(userDao.getUser())

            // fake api delay
            delay(1000L)

            val user = User(0, email = email, password = password)

            if (userDao.getUser(email, password) != null) {
                userDao.insertUser(user)
                _isAuthenticatedFlow.value = true
                return Result.success(Unit)
            }

            return Result.failure(Exception("No account found"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<Boolean> {
        return try {

            // fake api delay
            delay(1000L)

            val users = userDao.getAllUsers()

            userDao.insertUser(User(users.size+1, email = email, password = password))

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        userDao.deleteUser("0")
        _isAuthenticatedFlow.value = false
    }
}