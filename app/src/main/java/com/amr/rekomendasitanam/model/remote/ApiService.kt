package com.amr.rekomendasitanam.model.remote


import com.amr.rekomendasitanam.model.remote.response.RecommendationRequest
import com.amr.rekomendasitanam.model.remote.response.RecommendationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("predict")
    suspend fun postRecommendation(
        @Body request: RecommendationRequest
    ): RecommendationResponse
}

