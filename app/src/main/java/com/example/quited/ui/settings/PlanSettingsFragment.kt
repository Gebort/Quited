package com.example.quited.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.quited.databinding.FragmentPlanSettingsBinding
import com.example.quited.presentation.settings.SettingsEvent
import com.example.quited.presentation.settings.SettingsUiEvent
import com.example.quited.presentation.settings.SettinsViewModel
import com.example.quited.presentation.util.Date
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class PlanSettingsFragment : Fragment() {

    private var _binding: FragmentPlanSettingsBinding? = null
    private val binding get() = _binding!!

    val model: SettinsViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveFab.setOnClickListener {
            model.onEvent(SettingsEvent.Save)
        }

        binding.notificationsSwitch.setOnClickListener {
            model.onEvent(SettingsEvent.SwitchNotification)
        }

        binding.amountInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val valueStr = s.toString()
                val value = if (valueStr == "") 0 else valueStr.toInt()
                model.onEvent(SettingsEvent.EnteredStartAmount(value))
            }
        })

        binding.reduceToInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val valueStr = s.toString()
                val value = if (valueStr == "") 0 else valueStr.toInt()
                model.onEvent(SettingsEvent.EnteredEndAmount(value))
            }
        })

        binding.daysInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val valueStr = s.toString()
                val value = if (valueStr == "") 0 else valueStr.toInt()
                model.onEvent(SettingsEvent.EnteredDaysAmount(value))
            }
        })

        binding.startDateInput.setOnFocusChangeListener { _, b ->
            if (b) {
                val constraintsBuilder =
                        CalendarConstraints.Builder()
                                .setValidator(DateValidatorPointForward.now())
                val picker =
                        MaterialDatePicker.Builder.datePicker()
                                .setTitleText(R.string.start_date)
                                .setSelection(model.state.value.startDate.dateLong)
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                picker.addOnPositiveButtonClickListener { date: Long ->
                    model.onEvent(SettingsEvent.EnteredStartDate(Date.fromLocal(date)))
                }

                fragmentManager?.let { it1 -> picker.show(it1, "startDatePicker") }
                binding.startDateLayout.isActivated = false
                binding.startDateInput.clearFocus()
            }
        }

        binding.startTimeInput.setOnFocusChangeListener { _, b ->
            if (b) {
                val clockFormat = TimeFormat.CLOCK_24H

                val picker =
                        MaterialTimePicker.Builder()
                                .setTimeFormat(clockFormat)
                                .setTitleText(R.string.start_time)
                                .build()

                picker.addOnPositiveButtonClickListener {
                    val startTime = Date.fromLocalTime(picker.hour, picker.minute)
                    model.onEvent(SettingsEvent.EnteredStartTime(startTime))
                }

                fragmentManager?.let { it1 -> picker.show(it1, "timeStartPicker") }
                binding.startTimeInput.isActivated = false
                binding.startTimeInput.clearFocus()
            }
        }

        binding.endTimeInput.setOnFocusChangeListener { _, b ->
            if (b) {
                val clockFormat = TimeFormat.CLOCK_24H

                val picker =
                        MaterialTimePicker.Builder()
                                .setTimeFormat(clockFormat)
                                .setTitleText(R.string.end_time)
                                .build()

                picker.addOnPositiveButtonClickListener {
                    val endTime = Date.fromLocalTime(picker.hour, picker.minute)
                    model.onEvent(SettingsEvent.EnteredEndTime(endTime))
                }

                fragmentManager?.let { it1 -> picker.show(it1, "timeEndPicker") }
                binding.endTimeInput.isActivated = false
                binding.endTimeInput.clearFocus()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.uiEvents.collect { uiEvent ->
                    when (uiEvent) {
                        is SettingsUiEvent.PlanSaved -> {
                            val snackbar = Snackbar.make(binding.coordinatorLayout, R.string.plan_saved, Snackbar.LENGTH_SHORT)
                            val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
                            params.gravity = Gravity.BOTTOM
                            snackbar.view.layoutParams = params
                            snackbar.show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.state.collect { state ->
                    if (state.daysAmount.toString() != binding.daysInput.text.toString() && state.daysAmount != 0){
                        binding.daysInput.setText(state.daysAmount.toString())
                    }
                    if (state.endAmount.toString() != binding.reduceToInput.text.toString() && state.endAmount != 0){
                        binding.reduceToInput.setText(state.endAmount.toString())
                    }
                    if (state.startDate.dateStr != binding.startDateInput.text.toString()){
                        binding.startDateInput.setText(state.startDate.dateStr)
                    }
                    if (state.startTime.timeStr != binding.startTimeInput.text.toString()){
                        binding.startTimeInput.setText(state.startTime.timeStr)
                    }
                    if (state.endTime.timeStr != binding.endTimeInput.text.toString()){
                        binding.endTimeInput.setText(state.endTime.timeStr)
                    }
                    if (state.startAmount.toString() != binding.amountInput.text.toString() && state.startAmount != 0){
                        binding.amountInput.setText(state.startAmount.toString())
                    }

                    binding.notificationsSwitch.isChecked = state.notifications

                    binding.progressBar.isVisible = state.loading
                    binding.saveFab.isEnabled = !state.loading
                    binding.amountInput.isEnabled = !state.loading
                    binding.daysInput.isEnabled = !state.loading
                    binding.endTimeInput.isEnabled = !state.loading
                    binding.startTimeInput.isEnabled = !state.loading
                    binding.reduceToInput.isEnabled = !state.loading
                    binding.startDateInput.isEnabled = !state.loading
                    binding.notificationsSwitch.isEnabled = !state.loading
                }
            }
        }
    }
}