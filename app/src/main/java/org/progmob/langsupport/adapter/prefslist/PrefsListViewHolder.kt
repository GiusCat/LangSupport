package org.progmob.langsupport.adapter.prefslist


import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.PrefListItemBinding

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */
class PrefsListViewHolder(
    binding: PrefListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    val word = binding.wordPref
    val translation = binding.translationPref
    val starButton = binding.starButton
    val translatedFlag = binding.wordFlagPref
    val translationNumber = binding.translationNumber
}