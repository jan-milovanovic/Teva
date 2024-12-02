package com.blankcat.teva.repository

import com.blankcat.teva.data.dao.AppDao
import com.blankcat.teva.models.AppData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AppRepository @Inject constructor(private val appDao: AppDao) {

    val appDataFlow: Flow<AppData?> = appDao.getAppData()

    fun getInitialAppData(): AppData? {
        return runBlocking {
            appDao.getAppData().firstOrNull()
        }
    }

    suspend fun toggleDarkTheme(value: Boolean) {
        val current = appDataFlow.firstOrNull()
        appDao.updateAppData((current ?: AppData()).copy(isDarkTheme = value))
    }
}