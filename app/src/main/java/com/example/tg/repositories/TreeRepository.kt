package com.example.tg.repositories

// Retrofit Imports
import com.example.tg.api.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
// Models
import com.example.tg.models.TreeResponse
// Logging
import android.util.Log


class TreeRepository {
    fun getAllTrees(callback: TreeDataCallback) {
        RetrofitClient.instance.getAllTrees().enqueue(object : Callback<TreeResponse> {
            override fun onResponse(call: Call<TreeResponse>, response: Response<TreeResponse>) {
                if (response.isSuccessful) {
                    response.body()?.trees?.let { trees ->
                        Log.i("Success", "Data Returned")
                        callback.onSuccess(trees)
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

            override fun onFailure(call: Call<TreeResponse>, t: Throwable) {
                Log.e("API Error", "Failed to fetch tree data", t)
                callback.onError("Failed to fetch tree data: ${t.message}")
            }
        })
    }
}