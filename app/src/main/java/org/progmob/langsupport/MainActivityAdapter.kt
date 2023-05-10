package org.progmob.langsupport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MainActivityAdapter(context: Context, textViewResourceId: Int, wordsList: MutableList<ActivityData>) : ArrayAdapter<ActivityData> (context, textViewResourceId, wordsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        // creazione della lista
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.activity_list_item, parent, false)
        }
        val words = getItem(position)
        showWords(context, view!!, words)
        return view
    }

    fun showWords(context: Context, view: View, WordsData: ActivityData?) {
        if (WordsData != null) {

            val italianWord = view.findViewById<TextView>(R.id.italianWord)
            italianWord.text = WordsData.italianWord

            val translatedWord = view.findViewById<TextView>(R.id.translatedWord)
            translatedWord.text = WordsData.translatedWord

        }
    }
}
