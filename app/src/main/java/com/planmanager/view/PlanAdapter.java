package com.planmanager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.planmanager.R;
import com.planmanager.model.Plan;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlanAdapter extends ArrayAdapter<Plan> {
    private final Context context;
    private List<Plan> plans;
    private OnPlanClickListener listener;

    public interface OnPlanClickListener {
        void onToggleStatus(int planId);
        void onDelete(int planId);
    }

    public PlanAdapter(Context context, List<Plan> plans) {
        super(context, 0, plans);
        this.context = context;
        this.plans = plans;
    }

    public void setOnPlanClickListener(OnPlanClickListener listener) {
        this.listener = listener;
    }

    public void updatePlans(List<Plan> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Plan plan = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.plan_item, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvTime = convertView.findViewById(R.id.tvTime);
        CheckBox cbCompleted = convertView.findViewById(R.id.cbCompleted);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        tvTitle.setText(plan.getTitle());
        tvDescription.setText(plan.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeText = sdf.format(plan.getStartTime()) + " - " + sdf.format(plan.getEndTime());
        tvTime.setText(timeText);

        cbCompleted.setChecked(plan.isCompleted());
        cbCompleted.setOnClickListener(v -> {
            if (listener != null) {
                listener.onToggleStatus(plan.getId());
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(plan.getId());
            }
        });

        return convertView;
    }
}
