package com.example.translatortrainer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.translatortrainer.databinding.FragmentMappingBinding


class FragmentMapping : Fragment() {

    lateinit var binding: FragmentMappingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMappingBinding.inflate(layoutInflater)
        return binding.root
    }
}