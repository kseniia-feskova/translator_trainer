package com.example.translatortrainer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translatortrainer.R
import com.example.translatortrainer.data.HistoryListAdapter
import com.example.translatortrainer.data.WordEntity
import com.example.translatortrainer.databinding.FragmentHistoryBinding
import com.example.translatortrainer.databinding.FragmentMainBinding
import com.example.translatortrainer.viewmodel.HistoryViewModel
import com.example.translatortrainer.viewmodel.TranslationViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var adapter: HistoryListAdapter

    private val viewModel by viewModel<HistoryViewModel>() //change

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.startObserve()
        subscribeToObservable()
    }

    private fun initView() {
        lifecycleScope.launch {
            viewModel.translation.collect() {
                binding.translation.text = it
            }
        }

        binding.translateBtn.setOnClickListener {
            val text = binding.request.text.toString()
            viewModel.translate(text)
        }
//        lifecycleScope.launch {
//            viewModel.word.collect() {
//                viewModel.translate(it)
//            }
//        }
    }

    private fun subscribeToObservable() {
        viewModel.allWords.observe(viewLifecycleOwner) {
            initAdapter(it)
        }
    }

    private fun initAdapter(_history: List<WordEntity>) {
        binding.historyList.layoutManager = LinearLayoutManager(context)
        adapter = HistoryListAdapter(_history)
        binding.historyList.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMain()
    }
}
