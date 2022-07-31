package com.example.quited.ui.stats

import android.graphics.Color
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
import com.example.quited.domain.model.DayStat
import com.example.quited.presentation.stats.StatsViewModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
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
                        if (adapter!!.days != state.days) {
                            adapter!!.days = state.days
                            updateChartData(state.days)
                            adapter!!.notifyDataSetChanged()
                        }
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

    private fun updateChartData(data: List<DayStat>){

        val chartData: Array<AASeriesElement> = arrayOf(
                AASeriesElement()
                        .name(resources.getString(R.string.plan))
                        .data(data.map { it.maxAmount }.toTypedArray()),
                AASeriesElement()
                        .name(resources.getString(R.string.real))
                        .data(data.map { it.ciggsAmount }.toTypedArray())
                        .dataLabels(AADataLabels())
        )

        val labels: Array<String> = data.map { it.date.dateStr }.toTypedArray()

        val aaChartModel : AAChartModel = AAChartModel()
                .chartType(AAChartType.Area)
                .backgroundColor(Color.TRANSPARENT)
                .legendEnabled(false)
                .dataLabelsEnabled(false)
                .yAxisTitle("")
                .yAxisLabelsEnabled(false)
                .xAxisLabelsEnabled(false)
                .series(chartData.toList().toTypedArray())
                .categories(labels)

        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)

    }
}