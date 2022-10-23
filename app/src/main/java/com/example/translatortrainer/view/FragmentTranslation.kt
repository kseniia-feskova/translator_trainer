package com.example.translatortrainer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.translatortrainer.databinding.FragmentTranslationBinding

class FragmentTranslation : Fragment() {

    lateinit var binding: FragmentTranslationBinding
    // private val viewModel: TranslationViewModel = getViewModel()

    val languageSource = "Deutsch"
    val languageTarget = "Russian"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initView()
    }

    private fun inputText() {
    }

//    private fun initView() {
//        viewModel.getSearch(binding.request)
//        lifecycleScope.launch {
//            viewModel.word.collect() {
//                viewModel.translate(it)
//            }
//        }
//        lifecycleScope.launch {
//            viewModel.translation.collect() {
//                binding.translation.text = it
//            }
//        }
//    }

//        binding.translateBtn.setOnClickListener {
//            val text = binding.request.query.toString()
//            useTranslator(text)
//        }


//    private suspend fun newWord(text: String, translate: String) = withContext(Dispatchers.IO)
//    {
//        val size = (activity as MainActivity).database.historyDao().getAll().size
//        val new = WordEntity(size + 1, text, translate, languageSource, false)
//        (activity as MainActivity).database.historyDao().insertAll(new)
//    }
//
//    private suspend fun translate(text: String): String = withContext(Dispatchers.IO) {
//        val translation = translator.translate(text, Language.RUSSIAN, Language.GERMAN)
//        return@withContext translation.translatedText
//    }
//
//    private fun useTranslator(text: String): Flow<String> {
//        (context as LifecycleOwner).lifecycleScope.launch {
//            viewModel.translate(text)
//        }
//    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentTranslation()
    }
}