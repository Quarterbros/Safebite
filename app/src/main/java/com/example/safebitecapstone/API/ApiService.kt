package com.example.safebitecapstone.API


import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

//    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("process_input")
    fun postDetection(
        @Body detectionPost: DetectionPost
    ): Call<DetectionResponse>
}

