package org.progmob.langsupport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import org.progmob.langsupport.model.WordData

class MainActivityAdapter(
    context: Context,
    textViewResourceId: Int,
    wordsList: MutableList<WordData>
) : ArrayAdapter<WordData> (context, textViewResourceId, wordsList) {

    companion object {
        val MSG: String? = this::class.simpleName
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        // creazione della lista
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.activity_list_item, parent, false)
        }
        val words = getItem(position)
        showWords(view!!, words)
        return view
    }

    private fun showWords(view: View, wordData: WordData?) {
        if (wordData != null) {

            val word = view.findViewById<TextView>(R.id.foreign_word)
            word.text = wordData.word

            val translation = view.findViewById<TextView>(R.id.translated_word)
            translation.text = wordData.translation.toString()

        }
    }
}
