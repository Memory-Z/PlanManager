package com.planmanager.repository

import android.content.Context
import androidx.room.Room
import com.planmanager.model.Plan
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.CompletableFuture

class PlanRepository(context: Context) {
    private val planDao: PlanDao

    init {
        val database = Room.databaseBuilder(
            context,
            PlanDatabase::class.java,
            "plan_database"
        ).build()
        planDao = database.planDao()
    }

    fun getPlans(): Flowable<List<Plan>> {
        return planDao.getPlans()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addPlan(plan: Plan): CompletableFuture<Plan> {
        val future = CompletableFuture<Plan>()
        planDao.insertPlan(plan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    future.complete(plan.copy(id = it.toInt()))
                },
                {
                    future.completeExceptionally(it)
                }
            )
        return future
    }

    fun updatePlan(plan: Plan): CompletableFuture<Plan> {
        val future = CompletableFuture<Plan>()
        planDao.updatePlan(plan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    future.complete(plan)
                },
                {
                    future.completeExceptionally(it)
                }
            )
        return future
    }

    fun deletePlan(planId: Int): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        planDao.getPlanById(planId)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable(planDao::deletePlan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    future.complete(null)
                },
                {
                    future.completeExceptionally(it)
                }
            )
        return future
    }

    fun togglePlanStatus(planId: Int): CompletableFuture<Plan> {
        val future = CompletableFuture<Plan>()
        planDao.getPlanById(planId)
            .subscribeOn(Schedulers.io())
            .flatMap {
                val updatedPlan = it.copy(isCompleted = !it.isCompleted)
                planDao.updatePlan(updatedPlan)
                    .andThen(Maybe.just(updatedPlan))
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    future.complete(it)
                },
                {
                    future.completeExceptionally(it)
                }
            )
        return future
    }
}
