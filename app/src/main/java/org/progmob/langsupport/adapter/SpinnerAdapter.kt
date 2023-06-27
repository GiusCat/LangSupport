package org.progmob.langsupport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import org.progmob.langsupport.R
import org.progmob.langsupport.databinding.SpinnerItemBinding
import org.progmob.langsupport.util.LanguageManager

class SpinnerAdapter(context: Context) : ArrayAdapter<String>(
    context,
    R.layout.spinner_item,
    LanguageManager.getLanguages()) {
    private val lm = LanguageManager

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        return SpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
            .apply {
                langFlag.setImageResource(lm.flagOf(position))
                langText.text = lm.nameOf(position)
            }.root
    }

}