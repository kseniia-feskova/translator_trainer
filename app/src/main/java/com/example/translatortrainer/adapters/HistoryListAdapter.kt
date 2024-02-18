package com.example.translatortrainer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.translatortrainer.data.WordDiffCallback
import com.example.translatortrainer.data.WordEntity
import com.example.translatortrainer.databinding.ItemHistoryBinding

class HistoryListAdapter(
    history: List<WordEntity>,
) : ListAdapter<WordEntity, HistoryListAdapter.ViewHolder>(WordDiffCallback) {

    private val _history: MutableList<WordEntity> = history.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return _history.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(_history[position]) {
                binding.translation.text = this.translation
                binding.word.text = this.word
                binding.sourceLanguage.text = this.language
            }
        }
    }

    override fun submitList(list: MutableList<WordEntity>?) {
        if (list != null) {
            _history.clear()
            _history.addAll(list)
        }
        super.submitList(list)
    }

    inner class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

}