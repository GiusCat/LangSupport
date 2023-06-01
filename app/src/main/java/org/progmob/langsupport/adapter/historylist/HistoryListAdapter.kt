package org.progmob.langsupport.adapter.historylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.HistoryListItemBinding
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.util.LanguageManager
import java.util.Locale

class HistoryListAdapter (
    private var dataSet: List<WordData> = listOf(),
    // private val listener: (WordData) -> Unit
) : RecyclerView.Adapter<HistoryListViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HistoryListViewHolder {
        // Create a new view, which defines the UI of the list item
        val bind = HistoryListItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return HistoryListViewHolder(bind)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: HistoryListViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.mainFlag.setImageResource(LanguageManager.flagOf(Locale.getDefault().language))
        viewHolder.wordFlag.setImageResource(LanguageManager.flagOf(item.lang))
        viewHolder.word.text = item.word
        viewHolder.translation.text = item.translation[0]
        // viewHolder.root.setOnClickListener { listener(item) }
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