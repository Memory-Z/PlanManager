package com.planmanager.state;

import com.planmanager.model.Plan;
import java.util.List;

public class PlanState {
    private final boolean isLoading;
    private final List<Plan> plans;
    private final String error;

    public PlanState(boolean isLoading, List<Plan> plans, String error) {
        this.isLoading = isLoading;
        this.plans = plans;
        this.error = error;
    }

    public static PlanState initial() {
        return new PlanState(false, List.of(), null);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public String getError() {
        return error;
    }

    public PlanState copy(boolean isLoading, List<Plan> plans, String error) {
        return new PlanState(
                isLoading != null ? isLoading : this.isLoading,
                plans != null ? plans : this.plans,
                error != null ? error : this.error
        );
    }
}
