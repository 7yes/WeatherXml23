package com.jess.weatherpxml.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jess.weatherpxml.R
import com.jess.weatherpxml.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        showData()
        return binding.root
    }

    private fun showData() {
        val data = viewmodel.result.value
        binding.ivIcon.setImageResource(
            when (data?.weather?.get(0)?.icon.toString()) {
                "01d" -> R.drawable.m01d
                "02d" -> R.drawable.m02d
                "03d" -> R.drawable.m03d
                "04d" -> R.drawable.m04d
                "09d" -> R.drawable.m09d
                "10d" -> R.drawable.m10d
                "11d" -> R.drawable.m11d
                "13d" -> R.drawable.m13d
                "50d" -> R.drawable.m50d
                else -> {
                    R.drawable.m13d
                }
            }
        )
        binding.tvTemp.text = "${data?.temp.toString()} °F"
        binding.tvMain.text = data?.weather?.get(0)?.main
        binding.tvDescription.text = data?.weather?.get(0)?.description
        binding.tvFeelsLike.text = "Feels like: ${data?.feelsLike.toString()}"
        binding.tvVisibility.text = "Visibility: ${data?.visibility.toString()}"
        binding.tvClouds.text = "Clouds: ${data?.clouds.toString()}"
        binding.tvWind.text = "Wind: ${data?.windSpeed.toString()}"
        binding.tvCity.text = data?.city
        binding.tvCountry.text = data?.country
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}