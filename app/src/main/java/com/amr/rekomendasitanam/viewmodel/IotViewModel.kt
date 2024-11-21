package com.amr.rekomendasitanam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class IotViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val sensorRef = database.getReference("data_sensor")

    private val _nitrogen = MutableLiveData<Int>()
    val nitrogen: LiveData<Int> = _nitrogen

    private val _phosporus = MutableLiveData<Int>()
    val phosporus: LiveData<Int> = _phosporus

    private val _kalium = MutableLiveData<Int>()
    val kalium: LiveData<Int> = _kalium

    private val _kelembaban = MutableLiveData<Double>()
    val kelembaban: LiveData<Double> = _kelembaban

    private val _rainfall = MutableLiveData<Double>()
    val rainfall: LiveData<Double> = _rainfall

    private val _suhu = MutableLiveData<Double>()
    val suhu: LiveData<Double> = _suhu

    private val _ph = MutableLiveData<Double>()
    val ph: LiveData<Double> = _ph

    init {
        dataIot()
    }

    private fun dataIot() {
        sensorRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nitrogenValues = mutableListOf<Int>()
                val phosporusValues = mutableListOf<Int>()
                val kaliumValues = mutableListOf<Int>()
                val kelembabanValues = mutableListOf<Double>()
                val rainfallValues = mutableListOf<Double>()
                val suhuValues = mutableListOf<Double>()
                val phValues = mutableListOf<Double>()

                for (sensorSnapshot in snapshot.children) {
                    for (timeSnapshot in sensorSnapshot.children) {
                        val nitrogen = timeSnapshot.child("nitrogen").getValue(Int::class.java)
                        val phosporus = timeSnapshot.child("phosphorus").getValue(Int::class.java)
                        val kalium = timeSnapshot.child("kalium").getValue(Int::class.java)
                        val kelembaban = timeSnapshot.child("kelembaban_udara").getValue(Double::class.java)
                        val rainfall = timeSnapshot.child("rainfall").getValue(Double::class.java)
                        val suhu = timeSnapshot.child("suhu_udara").getValue(Double::class.java)
                        val ph = timeSnapshot.child("ph_tanah").getValue(Double::class.java)

                        nitrogen?.let { nitrogenValues.add(it) }
                        phosporus?.let { phosporusValues.add(it) }
                        kalium?.let { kaliumValues.add(it) }
                        kelembaban?.let { kelembabanValues.add(it) }
                        rainfall?.let { rainfallValues.add(it) }
                        suhu?.let { suhuValues.add(it) }
                        ph?.let { phValues.add(it) }
                    }
                }

                _nitrogen.value = cropRecommendation(nitrogenValues)
                _phosporus.value = cropRecommendation(phosporusValues)
                _kalium.value = cropRecommendation(kaliumValues)
                _kelembaban.value = cropRecommendation(kelembabanValues)
                _rainfall.value = cropRecommendation(rainfallValues)
                _suhu.value = cropRecommendation(suhuValues)
                _ph.value = cropRecommendation(phValues)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }

    private fun <T> cropRecommendation(values: List<T>): T? {
        return values.groupBy { it }.maxByOrNull { it.value.size }?.key
    }
}
