package com.example.etmolexis

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Origin (

    var originLanguage    : String,
    var originDescription : String,
    var relatedWords      : ArrayList<String>

)