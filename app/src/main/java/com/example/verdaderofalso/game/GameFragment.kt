package com.example.verdaderofalso.game

import android.os.Build
import android.os.Build.VERSION.SDK
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.verdaderofalso.R
import com.example.verdaderofalso.databinding.FragmentGameBinding

class GameFragment : Fragment() {
//definicion del binding y viewModel
    lateinit var binding: FragmentGameBinding
    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

//se enlaza el observador
        viewModel= ViewModelProvider(this).get(GameViewModel::class.java)
//databinding
        binding.gameViewModel= viewModel
        binding.lifecycleOwner= this

/*
//Se crean los observadores
        viewModel.pregunta.observe(viewLifecycleOwner, Observer {newPregunta->
            binding.textViewPregunta.text=newPregunta
        })
        viewModel.correctas.observe(viewLifecycleOwner, Observer {newCorrectas->
            binding.textViewCorrectas.text= newCorrectas.toString()
        })
        viewModel.temporizador.observe(viewLifecycleOwner, Observer { newTemp->
            binding.textViewTemporizador.text= DateUtils.formatElapsedTime(newTemp).toString()
        })

        viewModel.inCorrectas.observe(viewLifecycleOwner, Observer { newIncorretas->
            binding.textViewIncorrectas.text= newIncorretas.toString()
       })
*/
        viewModel.eventoFinalizar.observe(viewLifecycleOwner, Observer { newEvent->
            if(newEvent) {//finalizo el juego
                findNavController(this).navigate(GameFragmentDirections.actionGameFragmentToScoreFragment
                (viewModel.inCorrectas.value?:0, viewModel.correctas.value?:0, viewModel.fan.value?:""))
                viewModel.juegoTerminadoCompletamente()
            }
        })
        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { event->
            if(event!= GameViewModel.BuzzType.NO_BUZZ) {
                buzz(event.pattern)
                viewModel.buzzCompleto()
            }
        })

//respuestas correctas e incorrectas
        var respuestaCorrecta=true
        viewModel.vof.observe(viewLifecycleOwner, Observer { newVoF->
            respuestaCorrecta= newVoF
        })
        binding.imageButtonVerdadero.setOnClickListener {
            if(respuestaCorrecta) //true-> correcta
                viewModel.respuestaCorrecta()
            else                  //false -> incorrecta
                viewModel.respuestaIncorrecta()
        }
        binding.imageButtonFalso.setOnClickListener {
            if(respuestaCorrecta) //true -> incorrecta
                viewModel.respuestaIncorrecta()
            else                  //false -> correcta
                viewModel.respuestaCorrecta()
        }
        return binding.root
    }

//
    private fun buzz(pattern: LongArray){
        val buzzer= activity?.getSystemService<Vibrator>()
        buzzer.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            }else {
                buzzer?.vibrate(pattern, -1)
            }
        }
    }
}