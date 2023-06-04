package org.progmob.langsupport.adapter.searchlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import org.progmob.langsupport.R
import org.progmob.langsupport.databinding.SearchListItemBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData


class SearchListAdapter(
    private var viewModel: DataViewModel,
    private var context: Context,
    private var dataSet: List<WordData> = listOf(),
    private val listener: (WordData) -> Unit
) : RecyclerView.Adapter<SearchListViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SearchListViewHolder {
        // Create a new view, which defines the UI of the list item
        val bind = SearchListItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return SearchListViewHolder(bind)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: SearchListViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.textView.text = item.word
        viewHolder.root.setOnClickListener { listener(item) }
        viewHolder.button.setOnClickListener {
            viewModel.updateFav(viewHolder.textView.text.toString(), false)
            Toast.makeText(context, "Aggiunta ai Preferiti!", Toast.LENGTH_LONG).show()
            //viewHolder.button.setBackgroundResource(R.drawable.full_star_24
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setWordsList(l: List<WordData>) {
        val oldList = dataSet.also { dataSet = l }
        if(oldList.isEmpty()) {
            notifyItemRangeInserted(0, l.size)
            return
        }
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = l.size

            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                oldList[oldPos].word == l[newPos].word

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                oldList[oldPos].word == l[newPos].word

        }).dispatchUpdatesTo(this)
    }
}
