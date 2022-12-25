package com.example.nasa_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.nasa_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        // this app configuration to prevent the back button from appearing
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment,
            )
        )
        setupActionBarWithNavController(navController , appBarConfiguration)
    }


    override fun onSupportNavigateUp(): Boolean {
        return   binding.root.findNavController().navigateUp() || super.onSupportNavigateUp()
    }



}