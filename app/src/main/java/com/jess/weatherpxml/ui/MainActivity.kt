package com.jess.weatherpxml.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jess.weatherpxml.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView ( binding.root )
    }
}