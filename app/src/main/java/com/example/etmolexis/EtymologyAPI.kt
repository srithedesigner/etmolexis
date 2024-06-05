package com.example.etmolexis

import retrofit2.Response

interface EtymologyAPI {
    fun getEtymology(word: String): Response<WordData>
}