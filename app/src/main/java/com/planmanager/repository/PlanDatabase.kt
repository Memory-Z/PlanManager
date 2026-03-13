package com.planmanager.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.planmanager.model.Plan

@Database(entities = [Plan::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class PlanDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
}
