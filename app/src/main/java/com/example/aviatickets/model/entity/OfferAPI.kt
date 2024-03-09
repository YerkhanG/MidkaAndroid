package com.example.aviatickets.model.entity

import com.google.gson.annotations.SerializedName

data class OfferAPI (
    val id : String,
    val price : Int,
    @SerializedName("flight") val flight: Flight
)