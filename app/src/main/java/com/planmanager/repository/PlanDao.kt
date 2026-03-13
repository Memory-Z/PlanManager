package com.planmanager.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.planmanager.model.Plan
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans")
    fun getPlans(): Flowable<List<Plan>>

    @Insert
    fun insertPlan(plan: Plan): Maybe<Long>

    @Update
    fun updatePlan(plan: Plan): Completable

    @Delete
    fun deletePlan(plan: Plan): Completable

    @Query("SELECT * FROM plans WHERE id = :planId")
    fun getPlanById(planId: Int): Maybe<Plan>

    @Query("UPDATE plans SET isCompleted = :completed WHERE id = :planId")
    fun updatePlanStatus(planId: Int, completed: Boolean): Completable
}
