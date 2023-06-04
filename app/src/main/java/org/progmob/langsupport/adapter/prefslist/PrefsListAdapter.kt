package org.progmob.langsupport.adapter.prefslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.ActivityListPrefsItemBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

class PrefsListAdapter(
    private var viewModel: DataViewModel,
    private var dataSet: List<WordData> = listOf()
) : RecyclerView.Adapter<PrefsListViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PrefsListViewHolder {
        // Create a new view, which defines the UI of the list item
        val bind = ActivityListPrefsItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PrefsListViewHolder(bind)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: PrefsListViewHolder, position: Int) {

        val item = dataSet[position]
        viewHolder.word.text = item.word
        viewHolder.translated.text = item.translation.toString()
        viewHolder.button.setOnClickListener {

            viewModel.updateFav(viewHolder.word.text.toString(), true)
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
