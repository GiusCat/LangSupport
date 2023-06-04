package org.progmob.langsupport.adapter.prefslist


import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.ActivityListPrefsItemBinding
import org.progmob.langsupport.databinding.SearchListItemBinding

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */
class PrefsListViewHolder(
    binding: ActivityListPrefsItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    val word = binding.italianWord
    val translated = binding.translatedWord
    val button = binding.starButton
    val root = binding.root

    fun addFavourite(){

        button.setOnClickListener {

        }
    }
}