package com.saif.posttest4pmob

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CitizenEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun citizenDao(): CitizenDao
}
