package com.example.appcursos.ui.cuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CuentaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "CUENTA"
    }
    val text: LiveData<String> = _text
}