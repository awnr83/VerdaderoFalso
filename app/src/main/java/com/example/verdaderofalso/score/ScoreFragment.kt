package com.example.verdaderofalso.score

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.verdaderofalso.R
import com.example.verdaderofalso.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {

    lateinit var binding: FragmentScoreBinding
    lateinit var viewModel: ScoreViewModel
    lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)

//definicion del factory y asignacion al viewModel
        val args= ScoreFragmentArgs.fromBundle(arguments!!)
        viewModelFactory= ScoreViewModelFactory(args.incorrectas, args.correctas, args.fan)
        viewModel= ViewModelProvider(this,viewModelFactory).get(ScoreViewModel::class.java)
        binding.scoreViewModel=viewModel


//        binding.textViewCorrectas.text= args.correctas.toString()
//        binding.textViewIncorrectas.text= args.incorrectas.toString()
//        binding.textViewFan.text = args.fan

        binding.buttonJugar.setOnClickListener {
            findNavController(this).navigate(ScoreFragmentDirections.actionScoreFragmentToGameFragment())
        }
        binding.buttonSalir.setOnClickListener {
            findNavController(this).navigate(ScoreFragmentDirections.actionScoreFragmentToPrincipalFragment())
        }
        return binding.root
    }
}