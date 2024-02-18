package com.example.translatortrainer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.translatortrainer.databinding.FragmentMappingBinding


class FragmentMapping : Fragment() {

    lateinit var binding: FragmentMappingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).hideBottomMenu()
        binding = FragmentMappingBinding.inflate(layoutInflater)
        binding.wip.backBtn.setOnClickListener {
            (requireActivity() as MainActivity).getNavigator().navigateUp()
        }
        return binding.root
    }
}