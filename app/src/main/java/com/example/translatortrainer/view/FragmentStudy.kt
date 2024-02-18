package com.example.translatortrainer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.translatortrainer.R
import com.example.translatortrainer.databinding.FragmentStudyBinding

class FragmentStudy : Fragment() {

    private lateinit var binding: FragmentStudyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).showBottomMenu()
        binding = FragmentStudyBinding.inflate(layoutInflater)
        handleNavigation()
        return binding.root
    }

    private fun handleNavigation(){
        binding.mapping.setOnClickListener {
            (requireActivity() as MainActivity).getNavigator().navigate(R.id.fragmentMapping)
        }
    }
}