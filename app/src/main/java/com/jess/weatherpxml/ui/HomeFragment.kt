package com.jess.weatherpxml.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
        binding.btnChangeCity.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_startFragment)
        }
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
                "01n" -> R.drawable.m01n
                "02n" -> R.drawable.m02n
                "03n" -> R.drawable.m03n
                "04n" -> R.drawable.m04n
                "09n" -> R.drawable.m09n
                "10n" -> R.drawable.m10n
                "11n" -> R.drawable.m11n
                "13n" -> R.drawable.m13n
                "50n" -> R.drawable.m50n
                else -> {
                    R.drawable.m13d
                }
            }
        )
        binding.tvTemp.text = getString(R.string.faren, data?.temp.toString())
        binding.tvMain.text = data?.weather?.get(0)?.main
        binding.tvDescription.text = data?.weather?.get(0)?.description
        binding.tvFeelsLike.text = getString(R.string.feels_like, data?.feelsLike.toString())
        binding.tvVisibility.text = getString(R.string.visibility, data?.visibility.toString())
        binding.tvClouds.text = getString(R.string.clouds, data?.clouds.toString())
        binding.tvWind.text = getString(R.string.wind, data?.windSpeed.toString())
        binding.tvCity.text = data?.city
        binding.tvCountry.text = data?.country
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.rotationAnim(binding.ivIcon)
        viewmodel.slideIcon(binding.ivIcon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}