package com.example.translatortrainer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translatortrainer.adapters.HistoryListAdapter
import com.example.translatortrainer.databinding.FragmentMainBinding
import com.example.translatortrainer.utils.Language
import com.example.translatortrainer.viewmodel.HistoryViewModel
import io.ktor.util.*
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

    override fun onResume() {
        super.onResume()
        handleSourceLanguage()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleSourceLanguage()
        initAdapter()
        initView()
        viewModel.startHistoryObserve()
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

    private fun handleSourceLanguage() {
        binding.selectedLanguage.setSimpleItems(
            arrayOf(
                Language.GERMAN.name.toLowerCasePreservingASCIIRules(),
                Language.FRENCH.name.toLowerCasePreservingASCIIRules(),
                Language.ENGLISH.name.toLowerCasePreservingASCIIRules()
            )
        )
        binding.selectedLanguage.setSelection(0)
        binding.selectedLanguage.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            viewModel.setLanguage(selectedItem)
        }
        binding.selectedTranslation.setSimpleItems(
            arrayOf(
                Language.UKRAINIAN.name.toLowerCasePreservingASCIIRules()
            )
        )
        binding.selectedTranslation.setSelection(0)
    }
}
