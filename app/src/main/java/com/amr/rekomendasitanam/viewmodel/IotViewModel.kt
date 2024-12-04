package com.amr.rekomendasitanam.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IotViewModel : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val _nitrogen = MutableLiveData<Int>()
    val nitrogen: LiveData<Int> = _nitrogen

    private val _phosporus = MutableLiveData<Int>()
    val phosporus: LiveData<Int> = _phosporus

    private val _kalium = MutableLiveData<Int>()
    val kalium: LiveData<Int> = _kalium

    private val _suhu = MutableLiveData<Double>()
    val suhu: LiveData<Double> = _suhu

    private val _kelembaban = MutableLiveData<Double>()
    val kelembaban: LiveData<Double> = _kelembaban

    private val _rainfall = MutableLiveData<Double>()
    val rainfall: LiveData<Double> = _rainfall

    private val _ph = MutableLiveData<Double>()
    val ph: LiveData<Double> = _ph

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        fetchLatestSensorData()
    }

    private fun fetchLatestSensorData() {
        // Mengambil kunci tanggal (misalnya "2024-10-10")
        database.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val latestDate = snapshot.children.firstOrNull() // Mengambil tanggal terakhir
                    if (latestDate != null) {
                        // Mengambil kunci waktu dari tanggal terbaru (misalnya "17:00:05")
                        val latestTime = latestDate.children.maxByOrNull { it.key ?: "" } // Mengambil waktu terakhir
                        val dataSensor = latestTime?.child("data_sensor")

                        // Mengambil data sensor dari waktu terakhir
                        _nitrogen.value = dataSensor?.child("nitrogen")?.getValue(Int::class.java) ?: 0
                        _phosporus.value = dataSensor?.child("phosphorus")?.getValue(Int::class.java) ?: 0
                        _kalium.value = dataSensor?.child("kalium")?.getValue(Int::class.java) ?: 0
                        _suhu.value = dataSensor?.child("suhu_udara")?.getValue(Double::class.java) ?: 0.0
                        _kelembaban.value = dataSensor?.child("kelembaban_udara")?.getValue(Double::class.java) ?: 0.0
                        _rainfall.value = dataSensor?.child("rainfall")?.getValue(Double::class.java) ?: 0.0
                        _ph.value = dataSensor?.child("ph_tanah")?.getValue(Double::class.java) ?: 0.0
                    }
                } catch (e: Exception) {
                    _error.value = "Gagal mengambil data sensor: ${e.message}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = "Database error: ${error.message}"
            }
        })
    }
}
