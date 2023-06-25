package org.progmob.langsupport.adapter.historylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.HistoryListItemBinding
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.util.LanguageManager

class HistoryListAdapter (
    private var dataSet: List<WordData> = listOf()
) : RecyclerView.Adapter<HistoryListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HistoryListViewHolder {
        // Create a new view, which defines the UI of the list item
        val bind = HistoryListItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return HistoryListViewHolder(bind)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: HistoryListViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.wordFlag.setImageResource(LanguageManager.flagOf(item.lang))
        viewHolder.word.text = item.word
        viewHolder.translation.text = item.translation[0]

        viewHolder.translationNumber.apply {
            visibility = if(item.translation.size > 1) View.VISIBLE else View.GONE
            text = "+${item.translation.size - 1}"
        }
    }

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
                oldList[oldPos].word == l[newPos].word &&
                oldList[oldPos].translation.size == l[newPos].translation.size

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                oldList[oldPos].word == l[newPos].word &&
                oldList[oldPos].translation.size == l[newPos].translation.size

        }).dispatchUpdatesTo(this)
    }
}