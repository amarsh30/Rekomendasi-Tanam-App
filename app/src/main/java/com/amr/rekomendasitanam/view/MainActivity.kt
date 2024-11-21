package com.amr.rekomendasitanam.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amr.rekomendasitanam.IotActivity
import com.amr.rekomendasitanam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInputData.setOnClickListener {
            confirmationIot()
        }
    }

    private fun confirmationIot() {
        val alertDialogBuilder = AlertDialog.Builder(this).apply {
            setTitle("Konfirmasi Alat IoT")
            setMessage("Apakah Kamu Sudah Menghubungkan Alat IoT?")
            setPositiveButton("Sudah") { dialog, _ ->
                dialog.dismiss()
                navigateToInputActivity()
            }
        }
        alertDialogBuilder.setNegativeButton("Belum") { dialog, _ ->
            dialog.dismiss()
            navigateToMainActivity()
        }
        alertDialogBuilder.show()

    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToInputActivity() {
        val intent = Intent(this, IotActivity::class.java)
        startActivity(intent)
    }
}

