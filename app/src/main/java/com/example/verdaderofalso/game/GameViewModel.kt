package com.example.verdaderofalso.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

//valores para el vibrador
private val INCORRECT_BUZZ= longArrayOf(50,50,50, 50,50,50,50) //dos vibraciones cortas
private val PANIC_BUZZ= longArrayOf(0,200) //una vibracion mas larga
private val NO_BUZ= longArrayOf(0)

class GameViewModel: ViewModel() {

//tipos de vibraciones
    enum class BuzzType(val pattern: LongArray){
        INCORRECTO(INCORRECT_BUZZ),
        PANIC(PANIC_BUZZ),
        NO_BUZZ(NO_BUZ)
    }

//variables para el temporizador
    companion object{
        const val INICIO= 0L
        const val UN_SEG= 1000L
        const val TIEMPO= 60000L
        const val PANIC_COUNT= 10000L //a partir de los 10s empieza el vibrador
    }
    private lateinit var timmer: CountDownTimer

//preguntas!!!
    data class Pregunta(
        val pregunta: String,
        val correcta: Boolean
    )
    private lateinit var preguntas: MutableList<Pregunta>

//variables LiveDate
    //cantidad de respuestas correctas e incorrectas
    private val _correctas= MutableLiveData<Int>()
    val correctas: LiveData<Int>
        get()=_correctas
    private val _inCorrectas= MutableLiveData<Int>()
    val inCorrectas: LiveData<Int>
        get()=_inCorrectas

    //preguntas
    private val _pregunta= MutableLiveData<String>()
    val pregunta:LiveData<String>
        get()=_pregunta

    //respuesta verdadera o falsa
    private val _vof= MutableLiveData<Boolean>()
    val vof: LiveData<Boolean>
        get()=_vof

    //palabra fan
    private val _fan= MutableLiveData<String>()
    val fan: LiveData<String>
        get()= _fan


    //temporizador
    private val _temporizador= MutableLiveData<Long>()
    val temporizador: LiveData<Long>
        get()=_temporizador
    //TanasformationMap
    val temporizadorString= Transformations.map(temporizador, {
        DateUtils.formatElapsedTime(it)
    })


//LiveData EVENTOS
    //evento finalizar
    private val _eventoFinalizar= MutableLiveData<Boolean>()
    val eventoFinalizar: LiveData<Boolean>
        get() = _eventoFinalizar

    //evento vibrador
    private val _eventbuzz= MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get()=_eventbuzz

//cantidad de preguntas
    private var cantidadPreguntas:Int=0

    init {
        resetLista()
        sigPregunta()
        _eventoFinalizar.value=false
        _correctas.value=0
        _inCorrectas.value=0
        _vof.value=true
        _fan.value=""
        iniciarTemporizador()
    }

    override fun onCleared() {
        super.onCleared()
        timmer.cancel()
    }
//funcionalidades internas
    private fun resetLista(){
        preguntas= mutableListOf(
            Pregunta("Solo un personaje de la serie tiene cinco dedos en lugar de cuatro?",false),
            Pregunta("Solo un personaje de la serie tiene cuatro dedos en lugar de cuatro?",true),
            Pregunta("Bart Simpson fue astronauta?", false),
            Pregunta("Los Simpson comenzaron siendo parte de otro progama?", true),
            Pregunta("Los Simpson no comenzaron siendo parte de otro progama?", false),
            Pregunta("Homero Simpson nacio el 12/05/65?", true),
            Pregunta("Homero Simpson nacio el 12/05/83?", false),
            Pregunta("El creador de Los Simpson elige al azar los nombres de los personajes? ", false),
            Pregunta("El creador de Los Simpson siempre elige los nombres de los personajes? ", true),
            Pregunta("Bart Simpson tiene 10 años de edad?", true),
            Pregunta("Bart Simpson tiene 11 años de edad?", false),
            Pregunta("El segundo nombre de Milhouse es Mushroom?", false),
            Pregunta("El segundo nombre de Milhouse es Mussolini?", true),
            Pregunta("Margaret Simpson nacio el 19/04/88?", true),
            Pregunta("Margaret Simpson nacio el 13/04/88?", false),
            Pregunta("En la versión de Los Simpson emitida en paises arabes, Homero bebe soda y no cerveza?", true),
            Pregunta("En la versión de Los Simpson emitida en paises arabes, Homero bebe cerveza y no soda?", false),
            Pregunta("Marge Bouvier nacio el 01/10/56?", true),
            Pregunta("Marge Bouvier nacio el 10/10/56?", false),
            Pregunta("Krusty El payaso fue originalmente pensado como el vecino famoso y rico de Homero?", false),
            Pregunta("Krusty El payaso nunca fue originalmente pensado como el vecino famoso y rico de Homero?", true),
            Pregunta("Abraham Simpson nacio el 18/12/27?", true),
            Pregunta("Abraham Simpson nacio el 18/12/28?", false),
            Pregunta("El intro del sofa es una estrategia para hacer mas o menos largo el episodio?", true),
            Pregunta("El intro del sofa no es una estrategia para hacer mas o menos largo el episodio?", false)
        )
        cantidadPreguntas= preguntas.size
        preguntas.shuffle()
    }
    private fun sigPregunta() {
        if (preguntas.isEmpty() || _temporizador.value == INICIO){
            definirFan()
            _eventoFinalizar.value=true
        }else {
            _pregunta.value = preguntas.get(0).pregunta
            _vof.value= preguntas.removeAt(0).correcta
        }
    }
    private fun iniciarTemporizador(){
        timmer= object: CountDownTimer(TIEMPO, UN_SEG){
            override fun onTick(millisUntilFinished: Long) {
                _temporizador.value= millisUntilFinished/ UN_SEG
                //entra en panic
                if(millisUntilFinished <= PANIC_COUNT)
                    _eventbuzz.value= BuzzType.PANIC
            }
            override fun onFinish() {
                _temporizador.value= INICIO
                definirFan()
                _eventoFinalizar.value=true
            }
        }
        timmer.start()
    }
    private fun definirFan(){
        val c= _correctas.value!!.toFloat()
        val cp= cantidadPreguntas.toFloat()
        val por= (c / cp * 100).toInt()
        if(100==por) {
            _fan.value = "Super ultra mega Fan!!"
        }else if(por >= 75){
            _fan.value = "Ultra Mega Fan!!"
        }else if(por >= 50){
            _fan.value = "Super Fan!!"
        }else if(por >= 25){
            _fan.value = "Casi Fan!!"
        }else
            _fan.value = "Ve la serie!!"
    }
//funcionalidades publicas
    fun respuestaCorrecta(){
        _correctas.value= _correctas.value!!.plus(1)
        sigPregunta()
    }
    fun respuestaIncorrecta(){
        _eventbuzz.value=BuzzType.INCORRECTO
        _inCorrectas.value= _inCorrectas.value!!.minus(1)
        sigPregunta()
    }
    fun juegoTerminado(){
        _eventoFinalizar.value=true
    }
    fun juegoTerminadoCompletamente(){
        _eventoFinalizar.value=false
    }
    fun buzzCompleto(){
        //termino el tiempo
        _eventbuzz.value= BuzzType.NO_BUZZ
    }
}