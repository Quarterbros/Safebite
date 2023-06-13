package com.example.safebitecapstone.API

import com.google.gson.annotations.SerializedName

data class DetectionResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("status")
	val status: String,
)

data class Result(

	@field:SerializedName("Data")
	val data: String,

	@field:SerializedName("Allergies Prediction")
	val allergiesPrediction: String,

	@field:SerializedName("Halal/Haram Prediction")
	val halalHaramPrediction: String,

	@field:SerializedName("Diseases Prediction")
	val diseasesPrediction: String
)
