package com.vespadev.brastlewarkapp.data.resources.api

import com.vespadev.brastlewarkapp.data.models.HeroesResponse
import retrofit2.http.GET

interface Api {
    @GET("rrafols/mobile_test/master/data.json")
    suspend fun heroes(): HeroesResponse
}