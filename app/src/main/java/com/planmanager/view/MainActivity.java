package com.planmanager.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.planmanager.R;
import com.planmanager.intent.PlanIntent;
import com.planmanager.model.Plan;
import com.planmanager.processor.PlanProcessor;
import com.planmanager.repository.PlanRepository;
import com.planmanager.state.PlanState;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etDescription;
    private Button btnAddPlan;
    private ListView lvPlans;
    private PlanAdapter adapter;

    private final PublishSubject<PlanIntent> intentSubject = PublishSubject.create();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private PlanState currentState = PlanState.initial();

    private final PlanRepository repository;
    private final PlanProcessor processor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new PlanRepository(this);
        processor = new PlanProcessor(repository);

        initViews();
        setupAdapter();
        setupObservers();
        loadPlans();
    }



    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnAddPlan = findViewById(R.id.btnAddPlan);
        lvPlans = findViewById(R.id.lvPlans);

        btnAddPlan.setOnClickListener(v -> addPlan());
    }

    private void setupAdapter() {
        adapter = new PlanAdapter(this, currentState.getPlans());
        adapter.setOnPlanClickListener(new PlanAdapter.OnPlanClickListener() {
            @Override
            public void onToggleStatus(int planId) {
                intentSubject.onNext(new PlanIntent.TogglePlanStatus(planId));
            }

            @Override
            public void onDelete(int planId) {
                intentSubject.onNext(new PlanIntent.DeletePlan(planId));
            }
        });
        lvPlans.setAdapter(adapter);
    }

    private void setupObservers() {
        disposables.add(
                intentSubject
                        .concatMap(intent -> processor.process(intent, currentState))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::render)
        );
    }

    private void loadPlans() {
        intentSubject.onNext(new PlanIntent.LoadPlans());
    }

    private void addPlan() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "请输入计划标题", Toast.LENGTH_SHORT).show();
            return;
        }

        Plan plan = new Plan(title, description, new Date(), new Date(), false);
        intentSubject.onNext(new PlanIntent.AddPlan(plan));

        // 清空输入框
        etTitle.setText("");
        etDescription.setText("");
    }

    private void render(PlanState state) {
        currentState = state;

        if (state.isLoading()) {
            // 显示加载状态
            Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show();
        } else if (state.getError() != null) {
            // 显示错误信息
            Toast.makeText(this, "错误: " + state.getError(), Toast.LENGTH_SHORT).show();
        } else {
            // 更新列表
            adapter.updatePlans(state.getPlans());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
