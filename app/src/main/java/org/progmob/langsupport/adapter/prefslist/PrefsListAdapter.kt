package org.progmob.langsupport.adapter.prefslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.progmob.langsupport.databinding.PrefListItemBinding
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.util.LanguageManager
import java.util.Locale

class PrefsListAdapter(
    private val starClickListener: (WordData) -> Unit
) : RecyclerView.Adapter<PrefsListViewHolder>() {

    private var dataSet: List<WordData> = listOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PrefsListViewHolder {
        val bind = PrefListItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PrefsListViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: PrefsListViewHolder, position: Int) {
        val item = dataSet[position]
        val lm = LanguageManager
        viewHolder.word.text = item.word
        viewHolder.translation.text = item.translation[0].toString()
        viewHolder.starButton.setOnClickListener {
            starClickListener(item)
        }
        viewHolder.translated_flag.setImageResource(lm.flagOf(item.lang))
        viewHolder.target_lang_flag.setImageResource(lm.flagOf(Locale.getDefault().language))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setFavWordsList(l: List<WordData>) {
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
