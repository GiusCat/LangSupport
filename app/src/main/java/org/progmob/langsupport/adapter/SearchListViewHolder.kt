package org.progmob.langsupport.adapter

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.SearchListItemBinding

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */
class SearchListViewHolder(binding: SearchListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val textView: TextView
    val button: Button

    init {
        // Define click listener for the ViewHolder's View
        textView = binding.word
        button = binding.favourite
    }
}