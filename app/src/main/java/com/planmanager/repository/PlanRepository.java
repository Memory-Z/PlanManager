package com.planmanager.repository;

import android.content.Context;

import androidx.room.Room;

import com.planmanager.model.Plan;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanRepository {
    private final PlanDao planDao;

    public PlanRepository(Context context) {
        PlanDatabase database = Room.databaseBuilder(
                context,
                PlanDatabase.class,
                "plan_database"
        ).build();
        planDao = database.planDao();
    }

    public Flowable<List<Plan>> getPlans() {
        return planDao.getPlans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public CompletableFuture<Plan> addPlan(Plan plan) {
        CompletableFuture<Plan> future = new CompletableFuture<>();
        planDao.insertPlan(plan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        id -> {
                            plan.setId((int) id.longValue());
                            future.complete(plan);
                        },
                        error -> future.completeExceptionally(error)
                );
        return future;
    }

    public CompletableFuture<Plan> updatePlan(Plan plan) {
        CompletableFuture<Plan> future = new CompletableFuture<>();
        planDao.updatePlan(plan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> future.complete(plan),
                        error -> future.completeExceptionally(error)
                );
        return future;
    }

    public CompletableFuture<Void> deletePlan(int planId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        planDao.getPlanById(planId)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(planDao::deletePlan)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> future.complete(null),
                        error -> future.completeExceptionally(error)
                );
        return future;
    }

    public CompletableFuture<Plan> togglePlanStatus(int planId) {
        CompletableFuture<Plan> future = new CompletableFuture<>();
        planDao.getPlanById(planId)
                .subscribeOn(Schedulers.io())
                .flatMap(plan -> {
                    boolean newStatus = !plan.isCompleted();
                    plan.setCompleted(newStatus);
                    return planDao.updatePlan(plan)
                            .andThen(Maybe.just(plan));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        plan -> future.complete(plan),
                        error -> future.completeExceptionally(error)
                );
        return future;
    }
}
