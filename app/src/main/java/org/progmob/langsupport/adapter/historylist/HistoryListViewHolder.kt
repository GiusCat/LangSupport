package org.progmob.langsupport.adapter.historylist

import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.HistoryListItemBinding

class HistoryListViewHolder(
    binding: HistoryListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    // val root = binding.root
    val wordFlag = binding.foreignFlag
    val mainFlag = binding.translatedFlag
    val word = binding.foreignWord
    val translation = binding.translatedWord
}