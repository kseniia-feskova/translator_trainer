package com.example.translatortrainer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translatortrainer.data.HistoryListAdapter
import com.example.translatortrainer.data.WordEntity
import com.example.translatortrainer.databinding.FragmentHistoryBinding


class FragmentHistory : Fragment() {

    private lateinit var adapter: HistoryListAdapter
    private lateinit var binding: FragmentHistoryBinding

    //private val viewModel by viewModel<HistoryViewModel>()


    companion object {
        @JvmStatic
        fun newInstance() = FragmentHistory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  viewModel.startObserve()
        //  subscribeToObservable()
    }
//
//    private fun subscribeToObservable() {
//        viewModel.allWords.observe(viewLifecycleOwner) {
//            initAdapter(it)
//        }
//    }

    private fun initAdapter(_history: List<WordEntity>) {
        binding.historyList.layoutManager = LinearLayoutManager(context)
        adapter = HistoryListAdapter(_history)
        binding.historyList.adapter = adapter
    }
}