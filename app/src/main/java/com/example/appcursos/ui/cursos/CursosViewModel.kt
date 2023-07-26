package com.example.appcursos.ui.cursos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CursosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "CURSOS"
    }
    val text: LiveData<String> = _text
}