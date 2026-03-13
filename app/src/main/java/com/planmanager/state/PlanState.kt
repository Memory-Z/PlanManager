package com.planmanager.state

import com.planmanager.model.Plan

data class PlanState(
    val isLoading: Boolean,
    val plans: List<Plan>,
    val error: String?
) {
    companion object {
        fun initial() = PlanState(
            isLoading = false,
            plans = emptyList(),
            error = null
        )
    }

    fun copy(
        isLoading: Boolean? = null,
        plans: List<Plan>? = null,
        error: String? = null
    ): PlanState {
        return PlanState(
            isLoading = isLoading ?: this.isLoading,
            plans = plans ?: this.plans,
            error = error ?: this.error
        )
    }
}
