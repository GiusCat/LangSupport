package org.progmob.langsupport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.ImageButton

class PrefsActivityAdapter(context: Context, textViewResourceId: Int, wordsList: MutableList<ActivityDataPrefs>) : ArrayAdapter<ActivityDataPrefs> (context, textViewResourceId, wordsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        // creazione della lista
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.activity_list_prefs_item, parent, false)
        }
        val words = getItem(position)
        showWords(view!!, words)
        return view
    }

    fun showWords(view: View, WordsData:ActivityDataPrefs?) {
        if (WordsData != null) {

            val italianWord = view.findViewById<TextView>(R.id.italianWord)
            italianWord.text = WordsData.italianWord

            val translatedWord = view.findViewById<TextView>(R.id.translatedWord)
            translatedWord.text = WordsData.translatedWord

            val butt = view.findViewById<ImageButton>(R.id.starButton)
            WordsData.imButton = butt
        }
    }
}