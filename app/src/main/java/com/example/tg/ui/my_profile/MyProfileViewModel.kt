package com.example.tg.ui.my_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyProfileViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is My Profile Fragment"
    }
    val text: LiveData<String> = _text
}
