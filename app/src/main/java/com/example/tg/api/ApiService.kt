package com.example.tg.api
import com.example.tg.models.TreeResponse
import retrofit2.Call
import retrofit2.http.GET


// This interface will be used to define the methods that relate to API calls
interface ApiService {
    @GET("tree")
    // Previously used TreeModel but this caused errors parsing the http status code
    fun getAllTrees(): Call<TreeResponse>

}