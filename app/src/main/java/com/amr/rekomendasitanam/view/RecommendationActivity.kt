package com.amr.rekomendasitanam.view

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amr.rekomendasitanam.R
import com.amr.rekomendasitanam.databinding.ActivityRecommendationBinding
import com.amr.rekomendasitanam.model.remote.response.RecommendationRequest
import com.amr.rekomendasitanam.viewmodel.IotViewModel
import com.amr.rekomendasitanam.viewmodel.RecommendationViewModel

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private val iotViewModel: IotViewModel by viewModels() // ViewModel untuk data IoT
    private val recommendationViewModel: RecommendationViewModel by viewModels() // ViewModel untuk prediksi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupObservers()
        btnRecommendation()
    }

    private fun setupSpinner() {
        val soilTypes = resources.getStringArray(R.array.soil_types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, soilTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSoilType.adapter = adapter

        binding.spSoilType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedSoilType = parent?.getItemAtPosition(position).toString()
                binding.spSoilType.tag = selectedSoilType

                if (selectedSoilType != getString(R.string.default_soil_type_text)) {
                    Toast.makeText(this@RecommendationActivity, "Tipe tanah yang dipilih: $selectedSoilType", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupObservers() {
        iotViewModel.nitrogen.observe(this) { binding.edNitrogen.setText(it.toString()) }
        iotViewModel.phosporus.observe(this) { binding.edPhosporous.setText(it.toString()) }
        iotViewModel.kalium.observe(this) { binding.edPotassium.setText(it.toString()) }
        iotViewModel.suhu.observe(this) { binding.edTemperature.setText(it.toString()) }
        iotViewModel.kelembaban.observe(this) { binding.edHumidity.setText(it.toString()) }
        iotViewModel.rainfall.observe(this) { binding.edRainfall.setText(it.toString()) }
        iotViewModel.ph.observe(this) { binding.edPh.setText(it.toString()) }

        recommendationViewModel.prediction.observe(this) { prediction ->
            Toast.makeText(this, "Hasil Prediksi: $prediction", Toast.LENGTH_SHORT).show()
            // Lakukan aksi lain jika diperlukan
        }

        recommendationViewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun btnRecommendation() {
        binding.btnRecommendation.setOnClickListener {
            val selectedSoilType = binding.spSoilType.tag as? String
            if (selectedSoilType.isNullOrEmpty() || selectedSoilType == getString(R.string.default_soil_type_text)) {
                Toast.makeText(this, "Silakan pilih jenis tanah.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nitrogen = binding.edNitrogen.text.toString().toIntOrNull()
            val phosphorus = binding.edPhosporous.text.toString().toIntOrNull()
            val potassium = binding.edPotassium.text.toString().toIntOrNull()
            val temperature = binding.edTemperature.text.toString().toDoubleOrNull()
            val humidity = binding.edHumidity.text.toString().toDoubleOrNull()
            val rainfall = binding.edRainfall.text.toString().toDoubleOrNull()
            val ph = binding.edPh.text.toString().toDoubleOrNull()

            if (nitrogen == null || phosphorus == null || potassium == null ||
                temperature == null || humidity == null || rainfall == null || ph == null) {
                Toast.makeText(this, "Semua field harus diisi dengan benar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val recommendationRequest = RecommendationRequest(
                Nitrogen = nitrogen,
                Phosporous = phosphorus,
                Potassium = potassium,
                temperature = temperature,
                humidity = humidity,
                rainfall = rainfall,
                ph = ph,
                soil_type_encode = selectedSoilType
            )

            recommendationViewModel.prediction(recommendationRequest)
        }
    }
}
