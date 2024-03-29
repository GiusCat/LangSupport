package org.progmob.langsupport.adapter.searchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.R
import org.progmob.langsupport.databinding.SearchListItemBinding
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.util.LanguageManager


class SearchListAdapter(
    private val rootClickListener: (WordData) -> Unit,
    private val starClickListener: (WordData) -> Unit,
    private val trashClickListener: (WordData) -> Unit
) : RecyclerView.Adapter<SearchListViewHolder>() {

    private var dataSet: List<WordData> = listOf()

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
        val lm = LanguageManager
        viewHolder.textView.text = item.word
        viewHolder.root.setOnClickListener { rootClickListener(item) }
        viewHolder.starButton.setImageResource(
            if(item.favourite) R.drawable.full_star_24 else R.drawable.empty_star_border_24)

        viewHolder.imageLang.setImageResource(lm.flagOf(item.lang))

        viewHolder.starButton.setOnClickListener {
            starClickListener(item)
            viewHolder.starButton.setImageResource(
                if(item.favourite) R.drawable.full_star_24 else R.drawable.empty_star_border_24)
        }

        viewHolder.trashButton.setOnClickListener {
            trashClickListener(item)
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
