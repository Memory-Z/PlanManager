package com.planmanager.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.planmanager.R
import com.planmanager.model.Plan
import java.text.SimpleDateFormat

class PlanAdapter(context: Context, private var plans: List<Plan>) :
    ArrayAdapter<Plan>(context, 0, plans) {

    interface OnPlanClickListener {
        fun onToggleStatus(planId: Int)
        fun onDelete(planId: Int)
    }

    private var listener: OnPlanClickListener? = null

    fun setOnPlanClickListener(listener: OnPlanClickListener) {
        this.listener = listener
    }

    fun updatePlans(plans: List<Plan>) {
        this.plans = plans
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val plan = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.plan_item, parent, false)

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
        val cbCompleted = view.findViewById<CheckBox>(R.id.cbCompleted)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        tvTitle.text = plan?.title
        tvDescription.text = plan?.description

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val timeText = "${sdf.format(plan?.startTime)}\n${sdf.format(plan?.endTime)}"
        tvTime.text = timeText

        plan?.let {
            cbCompleted.isChecked = it.isCompleted
            cbCompleted.setOnClickListener {
                listener?.onToggleStatus(it.id)
            }

            btnDelete.setOnClickListener {
                listener?.onDelete(it.id)
            }
        }

        return view
    }
}
