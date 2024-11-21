package com.amr.rekomendasitanam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amr.rekomendasitanam.model.remote.ApiConfig
import com.amr.rekomendasitanam.model.remote.response.RecommendationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationViewModel : ViewModel() {

    private val _prediction = MutableLiveData<String>()
    val prediction: LiveData<String> = _prediction

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun prediction(rec: RecommendationRequest
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = ApiConfig.apiService.postRecommendation(rec)

                withContext(Dispatchers.Main) {
                    _prediction.value = response.prediction
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Gagal memproses prediksi: ${e.message}"
                }
            }
        }
    }
}
