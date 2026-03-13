package com.planmanager.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.planmanager.model.Plan;

@Database(entities = {Plan.class}, version = 1, exportSchema = false)
public abstract class PlanDatabase extends RoomDatabase {
    public abstract PlanDao planDao();
}
