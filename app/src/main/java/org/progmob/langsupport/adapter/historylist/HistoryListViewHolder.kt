package org.progmob.langsupport.adapter.historylist

import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.HistoryListItemBinding

class HistoryListViewHolder(
    binding: HistoryListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    val wordFlag = binding.wordFlagHist
    val word = binding.wordHist
    val translation = binding.translationHist
    val translationNumber = binding.translationNumber
}