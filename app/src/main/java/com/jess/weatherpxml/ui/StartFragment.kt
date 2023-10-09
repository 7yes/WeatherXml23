package com.jess.weatherpxml.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.jess.weatherpxml.R
import com.jess.weatherpxml.core.hideKeyboard
import com.jess.weatherpxml.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// with more time we could create a separate class to request permission but because this is the
// only place where is required I keep it here
private const val CITY_NAME = "last_city"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = CITY_NAME
)

@AndroidEntryPoint
class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by activityViewModels<HomeViewModel>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    companion object {
        const val LOCATION_PERMISSIONS_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        fusedLocationProviderClient = FusedLocationProviderClient(requireContext())

        lifecycleScope.launch {
            readFromDataStore().collect() {
                if (it.isNotEmpty() && viewmodel.shouldOpenHome.value == true)
                    getCityForecast(it)
            }
        }

        viewmodel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.ERROR -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
                }
                ResultState.LOADING -> binding.progressCircular.isVisible = true
                is ResultState.SUCCESS -> {
                    viewmodel.updateCityData(state.results)
                    binding.progressCircular.isVisible = false
                    saveToDataStore(state.results.city)
                    if (viewmodel.shouldOpenHome.value == true)
                        navigateToHome()
                }
                is ResultState.ERROR_CONECTION -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        initListners()
        return binding.root
    }

    private fun initListners() {
        binding.btnNext.setOnClickListener {
            getCityForecast(binding.etCity.text.toString().lowercase())
            viewmodel.updateNavigationStatus(true)
        }
        binding.btnLocation.setOnClickListener {

                if (ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(), ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) { requestLocationPermission()
                    return@setOnClickListener
                }
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    viewmodel.getLatLonWeather(it.longitude, it.latitude)
                }
               viewmodel.updateNavigationStatus(true)
        }
        binding.etCity.doOnTextChanged { _, _, _, _ ->
            validateText()
            binding.btnNext.isEnabled = shouldEnableBtnGo()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_startFragment_to_homeFragment)
        viewmodel.updateNavigationStatus(false)
    }

    private fun saveToDataStore(city: String) {
        lifecycleScope.launch {
            requireContext().dataStore.edit {
                it[stringPreferencesKey("city")] = city
            }
        }
    }

    private fun readFromDataStore() = requireContext().dataStore.data.map {
        it[stringPreferencesKey("city")].orEmpty()
    }
    private fun getCityForecast(city: String) {
        viewmodel.getCityWeather(city)
        hideKeyboard()
    }
    private fun validateText() {
        val regex = "^[a-zA-Z\\s]+"
        val validText = binding.etCity.text.toString().matches(regex.toRegex())
        if (!validText) binding.etCity.error = "Just Letters"
    }
    private fun shouldEnableBtnGo() = binding.etCity.text.toString().length > 3
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSIONS_CODE
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


