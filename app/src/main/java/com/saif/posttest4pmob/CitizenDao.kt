package com.saif.posttest4pmob

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CitizenDao {
    @Insert suspend fun insert(c: CitizenEntity)
    @Query("SELECT * FROM citizen ORDER BY id ASC")
    fun getAll(): Flow<List<CitizenEntity>>
    @Query("DELETE FROM citizen") suspend fun clear()
}
