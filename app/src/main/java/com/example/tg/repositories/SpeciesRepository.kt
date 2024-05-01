package com.example.tg.repositories

import android.util.Log
import com.example.tg.api.RetrofitClient
import com.example.tg.models.SpeciesResponse
import com.example.tg.models.TreeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpeciesRepository {
    fun getAllSpecies(callback: SpeciesDataCallback) {
        RetrofitClient.instance.getSpecies().enqueue(object : Callback<SpeciesResponse> {
            override fun onResponse(call: Call<SpeciesResponse>, response: Response<SpeciesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.species?.let { species ->
                        Log.i("Success", "Data Returned")
                        callback.onSuccess(species)
                    } ?: run {
                        Log.e("API Error", "No data returned")
                        callback.onError("No data returned")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("API Error", "Failed to fetch tree data: $errorBody")
                    callback.onError("Failed to fetch tree data: $errorBody")
                }
            }

            override fun onFailure(call: Call<SpeciesResponse>, t: Throwable) {
                Log.e("API Error", "Failed to fetch tree data", t)
                callback.onError("Failed to fetch tree data: ${t.message}")
            }
        })
    }
}