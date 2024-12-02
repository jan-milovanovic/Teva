package com.blankcat.teva.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blankcat.teva.models.AppData
import kotlinx.coroutines.flow.Flow

@Dao interface AppDao {

    @Query("SELECT * FROM app_data LIMIT 1")
    fun getAppData(): Flow<AppData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAppData(appData: AppData)
}