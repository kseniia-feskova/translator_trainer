package com.example.translatortrainer.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.translatortrainer.adapters.HistoryListAdapter
import com.example.translatortrainer.databinding.FragmentMainBinding
import com.data.utils.Language
import com.example.translatortrainer.viewmodel.HistoryViewModel
import io.ktor.util.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val adapter: HistoryListAdapter = HistoryListAdapter(emptyList())

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
            hideKeyboard()
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
        binding.historyList.adapter = adapter
    }

    private fun handleSourceLanguage() {
        with(binding) {
            selectedLanguage.setSimpleItems(
                arrayOf(
                    Language.GERMAN.name.toLowerCasePreservingASCIIRules(),
                    Language.FRENCH.name.toLowerCasePreservingASCIIRules(),
                    Language.ENGLISH.name.toLowerCasePreservingASCIIRules()
                )
            )
            selectedLanguage.setSelection(0)
            selectedLanguage.setOnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position) as String
                viewModel.setLanguage(selectedItem)
            }
            selectedTranslation.setSimpleItems(
                arrayOf(Language.UKRAINIAN.name.toLowerCasePreservingASCIIRules())
            )
            selectedTranslation.setSelection(0)
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
