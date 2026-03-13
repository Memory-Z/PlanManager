package com.planmanager.intent;

import com.planmanager.model.Plan;

public sealed class PlanIntent {
    public static final class LoadPlans extends PlanIntent {
    }

    public static final class AddPlan extends PlanIntent {
        private final Plan plan;

        public AddPlan(Plan plan) {
            this.plan = plan;
        }

        public Plan getPlan() {
            return plan;
        }
    }

    public static final class UpdatePlan extends PlanIntent {
        private final Plan plan;

        public UpdatePlan(Plan plan) {
            this.plan = plan;
        }

        public Plan getPlan() {
            return plan;
        }
    }

    public static final class DeletePlan extends PlanIntent {
        private final int planId;

        public DeletePlan(int planId) {
            this.planId = planId;
        }

        public int getPlanId() {
            return planId;
        }
    }

    public static final class TogglePlanStatus extends PlanIntent {
        private final int planId;

        public TogglePlanStatus(int planId) {
            this.planId = planId;
        }

        public int getPlanId() {
            return planId;
        }
    }
}
