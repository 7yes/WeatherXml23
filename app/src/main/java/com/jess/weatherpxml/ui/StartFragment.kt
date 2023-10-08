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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jess.weatherpxml.R
import com.jess.weatherpxml.core.hideKeyboard
import com.jess.weatherpxml.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            getCityForecast()
//            if (checkPermissions()) { //todo
//                //permission Granted
//                getCityForecast()
//            } else {
//                // ask for permission
//                requestLocationPermission()
//            }
        }
        lifecycleScope.launch {
            readFromDataStore().collect(){
                withContext(Dispatchers.Main) {
                     Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
               if(it.isNotEmpty()&& viewmodel.shouldOpenHome.value == true)
                navigateToHome()
            }
        }
        binding.etCity.doOnTextChanged { _, _, _, _ ->
            validateText()
            binding.btnGo.isEnabled = shouldEnableBtnGo()
        }
        viewmodel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.ERROR -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
                }

                ResultState.LOADING -> binding.progressCircular.isVisible = true
                is ResultState.SUCCESS -> {
                    binding.progressCircular.isVisible = false
                   saveToDataStore(state.results.city)
                    if(viewmodel.shouldOpenHome.value == true)
                    navigateToHome()
                }

                is ResultState.ERROR_CONECTION -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }
    private fun navigateToHome() {
        findNavController().navigate(R.id.action_startFragment_to_homeFragment)
        viewmodel.updateNavigationStatus()
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

    private fun getCityForecast() {
        viewmodel.getCityWeather(binding.etCity.text.toString().lowercase())
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


