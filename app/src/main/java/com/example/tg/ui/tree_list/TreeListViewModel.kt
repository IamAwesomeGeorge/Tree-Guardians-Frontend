package com.example.tg.ui.tree_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TreeListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Tree List Fragment"
    }
    val text: LiveData<String> = _text
}