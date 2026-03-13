package com.planmanager.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.planmanager.R
import com.planmanager.databinding.ActivityListBinding
import com.planmanager.intent.PlanIntent
import com.planmanager.processor.PlanProcessor
import com.planmanager.repository.PlanRepository
import com.planmanager.state.PlanState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: PlanAdapter

    private val intentSubject = PublishSubject.create<PlanIntent>()
    private val disposables = CompositeDisposable()
    private var currentState = PlanState.initial()

    private lateinit var repository: PlanRepository
    private lateinit var processor: PlanProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = PlanRepository(this)
        processor = PlanProcessor(repository)

        initViews()
        setupAdapter()
        setupObservers()
        loadPlans()
    }

    private fun initViews() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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
        binding.lvPlans.adapter = adapter
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

    private fun render(state: PlanState) {
        currentState = state

        if (state.isLoading) {
            Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()
        } else if (state.error != null) {
            Toast.makeText(this, "错误: ${state.error}", Toast.LENGTH_SHORT).show()
        } else {
            adapter.updatePlans(state.plans)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}