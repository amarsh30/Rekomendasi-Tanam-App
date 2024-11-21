package com.amr.rekomendasitanam.model.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationRequest(
    val Nitrogen: Int,
    val Phosporous: Int,
    val Potassium: Int,
    val temperature: Double,
    val humidity: Double,
    val ph: Double,
    val rainfall: Double,
    val soil_type_encode: String
)

data class RecommendationResponse(
    @field:SerializedName("prediction")
    val prediction: String,
)