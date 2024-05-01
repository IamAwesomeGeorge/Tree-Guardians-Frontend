package com.example.tg.repositories
import com.example.tg.models.SpeciesModel

interface SpeciesDataCallback {
    fun onSuccess(species: List<SpeciesModel>)
    fun onError(errorMessage: String)
}