package org.progmob.langsupport.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import org.progmob.langsupport.ActivityDataPrefs
import org.progmob.langsupport.PrefsActivityAdapter
import org.progmob.langsupport.R

class PrefsFragment: Fragment() {

    var mycontext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mycontext = container?.context
        return inflater.inflate(R.layout.fragment_prefs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.searchPrefsButton)?.setOnClickListener {
            SearchWorInDB()
        }

        val words_list = initWordsList()

        if(words_list.size > 0){

            val adapter = PrefsActivityAdapter(mycontext!!, R.layout.activity_list_prefs_item, words_list)

            val listView : ListView? = getView()?.findViewById(R.id.listViewPrefs)
            listView?.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

    private fun initWordsList():MutableList<ActivityDataPrefs>{

        val activityList = mutableListOf<ActivityDataPrefs>()
        val imButt = getActivity()?.findViewById<ImageButton>(R.id.starButton)

        activityList.add(ActivityDataPrefs("apfel", "mela", imButt))
        activityList.add(ActivityDataPrefs("hallo", "ciao", imButt))
        activityList.add(ActivityDataPrefs("danke", "grazie", imButt))

        return activityList
    }

    private fun SearchWorInDB() {
        TODO("Not yet implemented")
    }
}