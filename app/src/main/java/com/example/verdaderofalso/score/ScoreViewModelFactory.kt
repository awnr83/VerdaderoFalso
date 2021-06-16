package com.example.verdaderofalso.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ScoreViewModelFactory(
        private val cantIncorrectas:Int,
        private val cantCorrectas:Int,
        private val nivelFan:String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(cantCorrectas, cantCorrectas, nivelFan) as T
        }
        throw IllegalArgumentException("Error Viewmodel Class")
    }

}