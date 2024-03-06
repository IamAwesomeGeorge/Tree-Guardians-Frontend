package com.example.tg.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    private val _more_text = MutableLiveData<String>().apply {
        value = ""
    }
    val text2: LiveData<String> = _more_text

}