package com.planmanager.processor

import com.planmanager.intent.PlanIntent
import com.planmanager.repository.PlanRepository
import com.planmanager.state.PlanState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class PlanProcessor(private val repository: PlanRepository) {
    private val scheduler = Schedulers.io()

    fun process(intent: PlanIntent, currentState: PlanState): Observable<PlanState> {
        return when (intent) {
            is PlanIntent.LoadPlans -> loadPlans(currentState)
            is PlanIntent.AddPlan -> addPlan(intent, currentState)
            is PlanIntent.UpdatePlan -> updatePlan(intent, currentState)
            is PlanIntent.DeletePlan -> deletePlan(intent, currentState)
            is PlanIntent.TogglePlanStatus -> togglePlanStatus(intent, currentState)
        }
    }

    private fun loadPlans(currentState: PlanState): Observable<PlanState> {
        return repository.getPlans()
            .map { plans -> currentState.copy(isLoading = false, plans = plans, error = null) }
            .onErrorReturn { error -> currentState.copy(isLoading = false, error = error.message) }
            .startWith(currentState.copy(isLoading = true))
    }

    private fun addPlan(intent: PlanIntent.AddPlan, currentState: PlanState): Observable<PlanState> {
        return Observable.fromFuture(repository.addPlan(intent.plan))
            .flatMap { repository.getPlans().take(1) }
            .map { plans -> currentState.copy(isLoading = false, plans = plans, error = null) }
            .onErrorReturn { error -> currentState.copy(isLoading = false, error = error.message) }
            .startWith(currentState.copy(isLoading = true))
    }

    private fun updatePlan(intent: PlanIntent.UpdatePlan, currentState: PlanState): Observable<PlanState> {
        return Observable.fromFuture(repository.updatePlan(intent.plan))
            .flatMap { repository.getPlans().take(1) }
            .map { plans -> currentState.copy(isLoading = false, plans = plans, error = null) }
            .onErrorReturn { error -> currentState.copy(isLoading = false, error = error.message) }
            .startWith(currentState.copy(isLoading = true))
    }

    private fun deletePlan(intent: PlanIntent.DeletePlan, currentState: PlanState): Observable<PlanState> {
        return Observable.fromFuture(repository.deletePlan(intent.planId))
            .flatMap { repository.getPlans().take(1) }
            .map { plans -> currentState.copy(isLoading = false, plans = plans, error = null) }
            .onErrorReturn { error -> currentState.copy(isLoading = false, error = error.message) }
            .startWith(currentState.copy(isLoading = true))
    }

    private fun togglePlanStatus(intent: PlanIntent.TogglePlanStatus, currentState: PlanState): Observable<PlanState> {
        return Observable.fromFuture(repository.togglePlanStatus(intent.planId))
            .flatMap { repository.getPlans().take(1) }
            .map { plans -> currentState.copy(isLoading = false, plans = plans, error = null) }
            .onErrorReturn { error -> currentState.copy(isLoading = false, error = error.message) }
            .startWith(currentState.copy(isLoading = true))
    }
}
