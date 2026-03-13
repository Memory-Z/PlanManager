package com.planmanager.processor;

import com.planmanager.intent.PlanIntent;
import com.planmanager.repository.PlanRepository;
import com.planmanager.state.PlanState;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;

public class PlanProcessor {
    private final PlanRepository repository;
    private final Scheduler scheduler;

    public PlanProcessor(PlanRepository repository) {
        this.repository = repository;
        this.scheduler = Schedulers.io();
    }

    public Observable<PlanState> process(PlanIntent intent, PlanState currentState) {
        return switch (intent) {
            case PlanIntent.LoadPlans loadPlans -> loadPlans(currentState);
            case PlanIntent.AddPlan addPlan -> addPlan(addPlan, currentState);
            case PlanIntent.UpdatePlan updatePlan -> updatePlan(updatePlan, currentState);
            case PlanIntent.DeletePlan deletePlan -> deletePlan(deletePlan, currentState);
            case PlanIntent.TogglePlanStatus togglePlanStatus -> togglePlanStatus(togglePlanStatus, currentState);
        };
    }

    private Observable<PlanState> loadPlans(PlanState currentState) {
        return repository.getPlans()
                .map(plans -> currentState.copy(false, plans, null))
                .onErrorReturn(error -> currentState.copy(false, null, error.getMessage()))
                .startWith(currentState.copy(true, null, null));
    }

    private Observable<PlanState> addPlan(PlanIntent.AddPlan intent, PlanState currentState) {
        return Observable.fromFuture(repository.addPlan(intent.getPlan()))
                .flatMap(plan -> repository.getPlans().take(1))
                .map(plans -> currentState.copy(false, plans, null))
                .onErrorReturn(error -> currentState.copy(false, null, error.getMessage()))
                .startWith(currentState.copy(true, null, null));
    }

    private Observable<PlanState> updatePlan(PlanIntent.UpdatePlan intent, PlanState currentState) {
        return Observable.fromFuture(repository.updatePlan(intent.getPlan()))
                .flatMap(plan -> repository.getPlans().take(1))
                .map(plans -> currentState.copy(false, plans, null))
                .onErrorReturn(error -> currentState.copy(false, null, error.getMessage()))
                .startWith(currentState.copy(true, null, null));
    }

    private Observable<PlanState> deletePlan(PlanIntent.DeletePlan intent, PlanState currentState) {
        return Observable.fromFuture(repository.deletePlan(intent.getPlanId()))
                .flatMap(v -> repository.getPlans().take(1))
                .map(plans -> currentState.copy(false, plans, null))
                .onErrorReturn(error -> currentState.copy(false, null, error.getMessage()))
                .startWith(currentState.copy(true, null, null));
    }

    private Observable<PlanState> togglePlanStatus(PlanIntent.TogglePlanStatus intent, PlanState currentState) {
        return Observable.fromFuture(repository.togglePlanStatus(intent.getPlanId()))
                .flatMap(plan -> repository.getPlans().take(1))
                .map(plans -> currentState.copy(false, plans, null))
                .onErrorReturn(error -> currentState.copy(false, null, error.getMessage()))
                .startWith(currentState.copy(true, null, null));
    }
}
