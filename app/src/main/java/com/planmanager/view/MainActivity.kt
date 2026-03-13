package com.planmanager.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.planmanager.R
import com.planmanager.intent.PlanIntent
import com.planmanager.model.Plan
import com.planmanager.processor.PlanProcessor
import com.planmanager.repository.PlanRepository
import com.planmanager.state.PlanState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAddPlan: Button
    private lateinit var lvPlans: ListView
    private lateinit var adapter: PlanAdapter

    private val intentSubject = PublishSubject.create<PlanIntent>()
    private val disposables = CompositeDisposable()
    private var currentState = PlanState.initial()

    private lateinit var repository: PlanRepository
    private lateinit var processor: PlanProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = PlanRepository(this)
        processor = PlanProcessor(repository)

        initViews()
        setupAdapter()
        setupObservers()
        loadPlans()
    }

    private fun initViews() {
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnAddPlan = findViewById(R.id.btnAddPlan)
        lvPlans = findViewById(R.id.lvPlans)

        btnAddPlan.setOnClickListener {
            addPlan()
        }
    }

    private fun setupAdapter() {
        adapter = PlanAdapter(this, currentState.plans)
        adapter.setOnPlanClickListener(object : PlanAdapter.OnPlanClickListener {
            override fun onToggleStatus(planId: Int) {
                intentSubject.onNext(PlanIntent.TogglePlanStatus(planId))
            }

            override fun onDelete(planId: Int) {
                intentSubject.onNext(PlanIntent.DeletePlan(planId))
            }
        })
        lvPlans.adapter = adapter
    }

    private fun setupObservers() {
        disposables.add(
            intentSubject
                .concatMap { intent -> processor.process(intent, currentState).toObservable() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state -> render(state) }
        )
    }

    private fun loadPlans() {
        intentSubject.onNext(PlanIntent.LoadPlans)
    }

    private fun addPlan() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "请输入计划标题", Toast.LENGTH_SHORT).show()
            return
        }

        val plan = Plan(
            title = title,
            description = description,
            startTime = Date(),
            endTime = Date(),
            isCompleted = false
        )
        intentSubject.onNext(PlanIntent.AddPlan(plan))

        // 清空输入框
        etTitle.text.clear()
        etDescription.text.clear()
    }

    private fun render(state: PlanState) {
        currentState = state

        if (state.isLoading) {
            // 显示加载状态
            Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()
        } else if (state.error != null) {
            // 显示错误信息
            Toast.makeText(this, "错误: ${state.error}", Toast.LENGTH_SHORT).show()
        } else {
            // 更新列表
            adapter.updatePlans(state.plans)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
