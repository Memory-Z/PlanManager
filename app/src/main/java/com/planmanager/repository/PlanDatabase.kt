package com.planmanager.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.planmanager.model.Plan

@Database(entities = [Plan::class], version = 1, exportSchema = false)
abstract class PlanDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
}
