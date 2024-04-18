package com.example.tg.repositories
import com.example.tg.models.TreeModel

interface TreeDataCallback {
    fun onSuccess(trees: List<TreeModel>)
    fun onError(errorMessage: String)
}