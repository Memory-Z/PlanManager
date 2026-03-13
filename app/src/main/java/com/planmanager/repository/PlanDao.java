package com.planmanager.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.planmanager.model.Plan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface PlanDao {
    @Query("SELECT * FROM plans")
    Flowable<List<Plan>> getPlans();

    @Insert
    Maybe<Long> insertPlan(Plan plan);

    @Update
    Completable updatePlan(Plan plan);

    @Delete
    Completable deletePlan(Plan plan);

    @Query("SELECT * FROM plans WHERE id = :planId")
    Maybe<Plan> getPlanById(int planId);

    @Query("UPDATE plans SET is_completed = :completed WHERE id = :planId")
    Completable updatePlanStatus(int planId, boolean completed);
}
