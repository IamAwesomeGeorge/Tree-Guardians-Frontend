package com.example.tg.models

// This class ensures the http status code passed back by the API does not
// cause errors when parsing the API response
data class TreeResponse(
    val status: Int,
    val trees: List<TreeModel>
)