package com.amr.rekomendasitanam.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amr.rekomendasitanam.R
import com.amr.rekomendasitanam.databinding.ActivityResultRecommendationBinding

class ResultRecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultRecommendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getPrediction()
        backtoHome()
    }

    private fun getPrediction() {

        // Mengambil hasil prediksi dari Intent
        val prediction = intent.getStringExtra("prediction")
        val nitrogen = intent.getIntExtra("Nitrogen", 0)
        val phosphorus = intent.getIntExtra("Phosphorus", 0)
        val potassium = intent.getIntExtra("Potassium", 0)
        val temperature = intent.getFloatExtra("Temperature", 0f)
        val humidity = intent.getFloatExtra("Humidity", 0f)
        val rainfall = intent.getFloatExtra("Rainfall", 0f)
        val ph = intent.getFloatExtra("pH", 0f)
        val soilType = intent.getStringExtra("SoilType")

        binding.apply {
            tvCrop.text = "$prediction"
            tvNilaiNirogen.text = "Nitrogen: $nitrogen"
            tvNilaiPhosporous.text = "Fosfor: $phosphorus"
            tvNilaiPotassium.text = "Kalium: $potassium"
            tvNilaiTemperature.text = "Suhu: $temperature (°C)"
            tvNilaiHumidity.text = "Kelembaban Udara: $humidity (%)"
            tvNilaiRainfall.text = "Curah Hujan: $rainfall (mm)"
            tvNilaiPh.text = "pH Tanah: $ph"
            tvNilaiSoil.text = "Jenis Tanah: $soilType"
        }


        val imagePredict = when (prediction) {
            "bawang merah" -> R.drawable.bawangmerah
            "Buncis" -> R.drawable.buncis
            "Jagung" -> R.drawable.jagung
            "Jeruk" -> R.drawable.jeruk
            "Kembang Kol" -> R.drawable.kembangkol
            "Padi" -> R.drawable.padi
            "Pisang" -> R.drawable.pisang
            "Tomat" -> R.drawable.tomat
            else -> {
                R.drawable.bawang
            }
        }

        binding.ivCrop.setImageResource(imagePredict)

    }

    private fun backtoHome() {
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}