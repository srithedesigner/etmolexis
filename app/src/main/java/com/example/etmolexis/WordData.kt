package com.example.etmolexis

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WordData (

    var word     : String,
    var wordType : String,
    var origins  : ArrayList<Origin> = arrayListOf()

)