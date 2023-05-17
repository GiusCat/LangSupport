package org.progmob.langsupport.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.SearchListItemBinding
import org.progmob.langsupport.model.WordData
import kotlin.math.max

class SearchListAdapter(private var dataSet: List<WordData>) :
    RecyclerView.Adapter<SearchListViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SearchListViewHolder {
        // Create a new view, which defines the UI of the list item
        val bind = SearchListItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return SearchListViewHolder(bind)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: SearchListViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].word
        viewHolder.button.setOnClickListener {
            Log.i("ViewHolder", "Clicked word ${dataSet[position]}")
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    // Returns the number of items changed in the list
    fun setWordsList(l: List<WordData>): Int {
        return max(dataSet.size, l.size).also { dataSet = l }
    }

}
