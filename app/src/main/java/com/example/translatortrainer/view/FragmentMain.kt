package com.example.translatortrainer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translatortrainer.data.HistoryListAdapter
import com.example.translatortrainer.databinding.FragmentMainBinding
import com.example.translatortrainer.viewmodel.HistoryViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: HistoryListAdapter

    private val viewModel by viewModel<HistoryViewModel>()

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
        initAdapter()
        viewModel.startObserve()
        subscribeToObservable()
    }

    private fun initView() {
        binding.translateBtn.setOnClickListener {
            val text = binding.request.text.toString()
            viewModel.translate(text)
        }
        lifecycleScope.launch {
            viewModel.translation.collect() {
                binding.translation.text = it
            }
        }
    }

    private fun subscribeToObservable() {
        viewModel.allWords.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }
    }

    private fun initAdapter() {
        binding.historyList.layoutManager = LinearLayoutManager(context)
        adapter = HistoryListAdapter(emptyList())
        binding.historyList.adapter = adapter
    }
}
