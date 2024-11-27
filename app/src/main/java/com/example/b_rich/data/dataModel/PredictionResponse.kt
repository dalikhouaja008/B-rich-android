package com.example.b_rich.data.dataModel

data class PredictionResponse(
    val predictions: Map<String, List<PredictionData>>,
    val start_date: String
)