package com.amr.rekomendasitanam

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amr.rekomendasitanam.databinding.ActivityIotBinding
import com.amr.rekomendasitanam.view.RecommendationActivity

class IotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIotBinding
    private val viewModel: IotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        btnInput()
    }

    private fun observeViewModel() {
        viewModel.nitrogen.observe(this) { value ->
            binding.tvNitrogen.text = value?.toString() ?: "-"
        }

        viewModel.phosporus.observe(this) { value ->
            binding.tvPhosporous.text = value?.toString() ?: "-"
        }

        viewModel.kalium.observe(this) { value ->
            binding.tvPotassium.text = value?.toString() ?: "-"
        }

        viewModel.kelembaban.observe(this) { value ->
            binding.tvHumidity.text = value?.toString() ?: "-"
        }

        viewModel.rainfall.observe(this) { value ->
            binding.tvRainfall.text = value?.toString() ?: "-"
        }

        viewModel.suhu.observe(this) { value ->
            binding.tvTemperature.text = value?.toString() ?: "-"
        }

        viewModel.ph.observe(this) { value ->
            binding.tvPH.text = value?.toString() ?: "-"
        }
    }

    private fun btnInput() {
        binding.btnRekomendasi.setOnClickListener {
            val nitrogen = binding.tvNitrogen.text.toString().toInt()
            val phosphorus = binding.tvPhosporous.text.toString().toInt()
            val potassium = binding.tvPotassium.text.toString().toInt()
            val temperature = binding.tvTemperature.text.toString().toFloat()
            val humidity = binding.tvHumidity.text.toString().toFloat()
            val rainfall = binding.tvRainfall.text.toString().toFloat()
            val ph = binding.tvPH.text.toString().toFloat()
            val intent = Intent(this, RecommendationActivity::class.java).apply {
                putExtra("nitrogen", nitrogen)
                putExtra("phosphorus", phosphorus)
                putExtra("kalium", potassium)
                putExtra("suhu_udara", temperature)
                putExtra("kelembaban_udara", humidity)
                putExtra("rainfall", rainfall)
                putExtra("ph_tanah", ph)
            }
            startActivity(intent)
        }
    }
}