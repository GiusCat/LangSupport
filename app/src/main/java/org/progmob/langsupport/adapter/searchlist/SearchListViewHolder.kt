package org.progmob.langsupport.adapter.searchlist

import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.SearchListItemBinding

class SearchListViewHolder(
    binding: SearchListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    val textView = binding.word
    val imageLang = binding.imageLang
    val starButton = binding.favourite
    val trashButton = binding.deleteWord
    val root = binding.root
}