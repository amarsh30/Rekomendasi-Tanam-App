package com.amr.rekomendasitanam.view

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amr.rekomendasitanam.R
import com.amr.rekomendasitanam.databinding.ActivityRecommendationBinding
import com.amr.rekomendasitanam.model.remote.response.RecommendationRequest
import com.amr.rekomendasitanam.viewmodel.RecommendationViewModel

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private val viewModel: RecommendationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFirebase()
        setupSpinner()
        setupObservers()
        btnRecommendation()
    }

    private fun getDataFirebase(){
        val nitrogen = intent.getIntExtra("nitrogen", 0)
        val phosporous = intent.getIntExtra("phosphorus", 0)
        val potassium = intent.getIntExtra("kalium", 0)
        val temperature = intent.getFloatExtra("suhu_udara", 0f)
        val humidity = intent.getFloatExtra("kelembaban_udara", 0f)
        val rainfall = intent.getFloatExtra("rainfall", 0f)
        val ph = intent.getFloatExtra("ph_tanah", 0f)

        binding.edNitrogen.setText(nitrogen.toString())
        binding.edPhosporous.setText(phosporous.toString())
        binding.edPotassium.setText(potassium.toString())
        binding.edTemperature.setText(temperature.toString())
        binding.edHumidity.setText(humidity.toString())
        binding.edRainfall.setText(rainfall.toString())
        binding.edPh.setText(ph.toString())
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
        viewModel.prediction.observe(this) { prediction ->
            val intent = Intent(this, ResultRecommendationActivity::class.java).apply {
                putExtra("prediction", prediction)
                putExtra("Nitrogen", binding.edNitrogen.text.toString().toInt())
                putExtra("Phosphorus", binding.edPhosporous.text.toString().toInt())
                putExtra("Potassium", binding.edPotassium.text.toString().toInt())
                putExtra("Temperature", binding.edTemperature.text.toString().toFloat())
                putExtra("Humidity", binding.edHumidity.text.toString().toFloat())
                putExtra("Rainfall", binding.edRainfall.text.toString().toFloat())
                putExtra("pH", binding.edPh.text.toString().toFloat())
                putExtra("SoilType", binding.spSoilType.tag as String)
            }
            startActivity(intent)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
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

            val nitrogenText = binding.edNitrogen.text.toString()
            val phosporousText = binding.edPhosporous.text.toString()
            val potassiumText = binding.edPotassium.text.toString()
            val temperatureText = binding.edTemperature.text.toString()
            val humidityText = binding.edHumidity.text.toString()
            val rainfallText = binding.edRainfall.text.toString()
            val phText = binding.edPh.text.toString()

            if (nitrogenText.isEmpty() || phosporousText.isEmpty() || potassiumText.isEmpty() ||
                temperatureText.isEmpty() || humidityText.isEmpty() || rainfallText.isEmpty() || phText.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Konversi Text dari EditText ke tipe data yang sesuai
            val nitrogen = nitrogenText.toInt()
            val phosphorus = phosporousText.toInt()
            val potassium = potassiumText.toInt()
            val temperature = temperatureText.toDouble() // Ubah ke Double
            val humidity = humidityText.toDouble() // Ubah ke Double
            val rainfall = rainfallText.toDouble() // Ubah ke Double
            val ph = phText.toDouble() // Ubah ke Double
            val soilType = selectedSoilType

            // Buat objek RecommendationRequest
            val recommendationRequest = RecommendationRequest(
                Nitrogen = nitrogen,
                Phosporous = phosphorus,
                Potassium = potassium,
                temperature = temperature,
                humidity = humidity,
                rainfall = rainfall,
                ph = ph,
                soil_type_encode = soilType
            )

            // Panggil fungsi prediction di ViewModel
            viewModel.prediction(recommendationRequest)

        }
    }
}
