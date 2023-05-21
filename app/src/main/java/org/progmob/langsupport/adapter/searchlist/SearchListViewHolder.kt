package org.progmob.langsupport.adapter.searchlist

import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.SearchListItemBinding

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */
class SearchListViewHolder(binding: SearchListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val textView = binding.word
    val button = binding.favourite
    val root = binding.root

    init {
        // Define click listener for the ViewHolder's View
        // textView = binding.word
        // button = binding.favourite
    }
}