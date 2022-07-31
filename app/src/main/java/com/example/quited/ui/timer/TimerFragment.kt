package com.example.quited.ui.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quited.R
import com.example.quited.databinding.FragmentTimerBinding
import com.example.quited.presentation.timer.AlarmBroadcastReceiver
import com.example.quited.presentation.timer.TimerEvent
import com.example.quited.presentation.timer.TimerUiEvent
import com.example.quited.presentation.timer.TimerViewModel
import com.example.quited.presentation.util.Date
import com.example.quited.presentation.util.Duration
import com.example.quited.ui.timer.animation.ProgressBarAnimation
import com.example.quited.ui.timer.animation.TimeTextAnimation
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    val binding get() = _binding!!

    private val model: TimerViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.smokeFab.setOnClickListener {
            model.onEvent(TimerEvent.saveCigg)
        }

        model.state.value.let { state ->
            binding.timerText.text = state.duration.timeStr
            binding.timerProgressBar.progress = ((state.maxDuration.timeLong - state.duration.timeLong)*100).toInt()/state.maxDuration.timeLong.toInt()
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                model.state.collect { state ->
                    binding.progressBar3.isVisible = state.loading
                    binding.counterText.text = String.format(resources.getString(R.string.ciggs), state.dayStat.ciggsAmount.toString(), state.dayStat.maxAmount.toString())
                    if (!state.planSet) {
                        binding.timerText.animation = null
                        binding.timerText.text = resources.getString(R.string.no_plan)
                        val anim = ProgressBarAnimation(binding.timerProgressBar, to = 0)
                        binding.timerProgressBar.startAnimation(anim)
                    }
                    else {
                        if (state.duration.timeLong <= 0) {
                            binding.timerText.animation = null
                            binding.timerText.text = resources.getString(R.string.you_can_smoke)
                            val animBar = ProgressBarAnimation(binding.timerProgressBar, to = 100)
                            binding.timerProgressBar.startAnimation(animBar)
                        } else {
                            val animText = TimeTextAnimation(binding.timerText, from = Duration.fromString(binding.timerText.text.toString()), to = state.duration)
                            binding.timerText.startAnimation(animText)
                            val to = ((state.maxDuration.timeLong - state.duration.timeLong)*100).toInt()/state.maxDuration.timeLong.toInt()
                            val animBar = ProgressBarAnimation(binding.timerProgressBar, to = to)
                            binding.timerProgressBar.startAnimation(animBar)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                model.uiEvent.collect { uiEvent ->
                     when (uiEvent) {
                         is TimerUiEvent.CiggSaved -> {
                             val snackbar = Snackbar.make(binding.coordinatorLayout, R.string.cigg_saved, Snackbar.LENGTH_LONG)
                                     .setAction(R.string.undo) {
                                         model.onEvent(TimerEvent.undoCigg)
                                     }
                             val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
                             params.gravity = Gravity.BOTTOM
                             snackbar.view.layoutParams = params
                             snackbar.show()
                         }
                         is TimerUiEvent.Notification -> {
//                             val alarmManager = activity?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                             val intent = Intent(activity?.applicationContext, AlarmBroadcastReceiver::class.java)
//                             val pendingIntent = PendingIntent.getBroadcast(activity?.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

//                             val date = uiEvent.date.toUTC()
//
//                             alarmManager.set(
//                                     AlarmManager.RTC_WAKEUP,
//                                     date.datestamp,
//                                     pendingIntent
//                             )
                         }
                         is TimerUiEvent.CancelNotification -> {
//                             val alarmManager = activity?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                             val intent = Intent(activity?.applicationContext, AlarmBroadcastReceiver::class.java)
//                             val pendingIntent = PendingIntent.getBroadcast(activity?.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//
//                             alarmManager.cancel(
//                                     pendingIntent
//                             )
                         }
                    }
                }
            }
        }


        return view
    }

}