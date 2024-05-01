package com.example.tg.api
import com.example.tg.models.SpeciesResponse
import com.example.tg.models.TreeResponse
import com.example.tg.models.TreeModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


// This interface will be used to define the methods that relate to API calls
interface ApiService {
    @GET("tree")
    // Previously used TreeModel but this caused errors parsing the http status code
    fun getAllTrees(): Call<TreeResponse>

    // Need to add additional methods such as POST, PUT AND DELETE


    @GET("species")
    fun getSpecies(): Call<SpeciesResponse>

    @POST("tree")
    fun createTree(@Body postData: TreeModel): Call<TreeResponse>

}