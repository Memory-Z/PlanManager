package com.planmanager.intent

import com.planmanager.model.Plan

sealed class PlanIntent {
    object LoadPlans : PlanIntent()

    data class AddPlan(val plan: Plan) : PlanIntent()

    data class UpdatePlan(val plan: Plan) : PlanIntent()

    data class DeletePlan(val planId: Int) : PlanIntent()

    data class TogglePlanStatus(val planId: Int) : PlanIntent()
}
