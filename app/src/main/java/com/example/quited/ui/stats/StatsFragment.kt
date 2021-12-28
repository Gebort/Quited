package com.example.quited.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quited.R
import com.example.quited.databinding.FragmentStatsBinding
import com.example.quited.presentation.stats.StatsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private val model: StatsViewModel by activityViewModels()
    private var adapter: DaysAdapter? = null

    @ExperimentalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = DaysAdapter(binding.rView, listOf())

        binding.rView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.rView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                model.state.collect { state ->
                    if (state.planSet){
                        binding.textPlan.text = resources.getString(R.string.plan)
                    }
                    else {
                        binding.textPlan.text = resources.getString(R.string.no_plan)
                    }

                    binding.cardView2.isVisible = state.planSet
                    binding.progressBar2.isVisible = state.loading

                    adapter?.let {
                        adapter!!.days = state.days
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                model.uiEvent.collect { uiEvent ->
                }
            }
        }

        return view
    }
}