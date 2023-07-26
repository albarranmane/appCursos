package com.example.appcursos.ui.miscursos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MiscursosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "MIS CURSOS"
    }
    val text: LiveData<String> = _text
}