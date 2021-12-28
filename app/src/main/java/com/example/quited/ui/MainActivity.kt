package com.example.quited.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.quited.R
import com.example.quited.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.fragmentContainerView))

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            selectFragment(item)
            false
        }
    }

    private fun selectFragment(item: MenuItem) {
        val selectedItem = binding.bottomNavigationView.selectedItemId

        if (item.itemId == selectedItem){
            return
        }

        val navController = findNavController(R.id.fragmentContainerView)

        if (selectedItem == -1)
            navController.navigate(item.itemId)
        else
            navController.navigate(
                    when (item.itemId) {
                        R.id.planSettingsFragment ->
                            if (selectedItem == R.id.timerFragment)
                                R.id.action_timerFragment_to_planSettingsFragment
                            else
                                R.id.action_statsFragment_to_planSettingsFragment
                        R.id.timerFragment ->
                            if (selectedItem == R.id.planSettingsFragment)
                                R.id.action_planSettingsFragment_to_timerFragment
                            else
                                R.id.action_statsFragment_to_timerFragment
                        R.id.statsFragment ->
                            if (selectedItem == R.id.planSettingsFragment)
                                R.id.action_planSettingsFragment_to_statsFragment
                            else
                                R.id.action_timerFragment_to_statsFragment
                        else -> item.itemId
                    })

        for (i in 0 until binding.bottomNavigationView.menu.size()) {
            val menuItem = binding.bottomNavigationView.menu.getItem(i)
            if (menuItem.itemId == item.itemId) menuItem.isChecked = true
        }
    }
    }