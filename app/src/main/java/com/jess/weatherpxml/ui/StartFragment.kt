package com.jess.weatherpxml.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jess.weatherpxml.R
import com.jess.weatherpxml.core.hideKeyboard
import com.jess.weatherpxml.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by viewModels<HomeViewModel>()

    companion object {
        const val LOCATION_PERMISSIONS_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        binding.btnGo.setOnClickListener {
            if (checkPermissions()) {
            //permission Granted
               getCityForecast()
            } else {
                // ask for permission
                requestLocationPermission()
            }
        }
        binding.etCity.doOnTextChanged { _, _, _, _ ->
            validateText()
           binding.btnGo.isEnabled = shouldEnableBtnGo()
        }

        return binding.root
    }

    private fun getCityForecast() {
        viewmodel.getCityWeather(binding.etCity.text.toString().lowercase())
        hideKeyboard()

        findNavController().navigate(R.id.action_startFragment_to_homeFragment)
    }

    private fun validateText() {
        val regex = "^[a-zA-Z\\s]+"
        val validText = binding.etCity.text.toString().matches(regex.toRegex())
        if (!validText) binding.etCity.error = "Just Letters"
    }

    private fun shouldEnableBtnGo() = binding.etCity.text.toString().length>3


    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSIONS_CODE
        )
    }

    private fun checkPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(), ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(), ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSIONS_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                //permission granted
                getCityForecast()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


