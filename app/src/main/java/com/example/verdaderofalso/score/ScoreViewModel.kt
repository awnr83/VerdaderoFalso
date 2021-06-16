package com.example.verdaderofalso.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(cantIncorrectas: Int, cantCorrectas: Int, nivelFan: String): ViewModel() {

//definicion y encapsulamiento
    private val _incorrectas= MutableLiveData<Int>()
    val incorrectas: LiveData<Int>
        get()=_incorrectas
    private val _correctas= MutableLiveData<Int>()
    val correctas: LiveData<Int>
        get()=_correctas
    private val _fan= MutableLiveData<String>()
    val fan:LiveData<String>
        get()=_fan

    init {
        _incorrectas.value= cantIncorrectas
        _correctas.value= cantCorrectas
        _fan.value= nivelFan
    }
}