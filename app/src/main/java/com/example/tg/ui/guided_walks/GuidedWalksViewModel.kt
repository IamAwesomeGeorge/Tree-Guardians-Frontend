package com.example.tg.ui.guided_walks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GuidedWalksViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Guided Walks Fragment"
    }
    val text: LiveData<String> = _text
}