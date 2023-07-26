package com.example.appcursos.ui.buscar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BuscarViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "BUSCAR"
    }
    val text: LiveData<String> = _text
}