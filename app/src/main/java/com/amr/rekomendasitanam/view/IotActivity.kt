package com.amr.rekomendasitanam.view
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amr.rekomendasitanam.databinding.ActivityIotBinding
import com.amr.rekomendasitanam.viewmodel.IotViewModel

class IotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIotBinding
    private val viewModel: IotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        btnMoveToRecommendation()
    }

    private fun setupObservers() {
        viewModel.nitrogen.observe(this) { binding.tvNitrogen.text = it.toString() }
        viewModel.phosporus.observe(this) { binding.tvPhosporous.text = it.toString() }
        viewModel.kalium.observe(this) { binding.tvPotassium.text = it.toString() }
        viewModel.suhu.observe(this) { binding.tvTemperature.text = it.toString() }
        viewModel.kelembaban.observe(this) { binding.tvHumidity.text = it.toString() }
        viewModel.rainfall.observe(this) { binding.tvRainfall.text = it.toString() }
        viewModel.ph.observe(this) { binding.tvPH.text = it.toString() }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun btnMoveToRecommendation() {
        binding.btnRekomendasi.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
    }
}
