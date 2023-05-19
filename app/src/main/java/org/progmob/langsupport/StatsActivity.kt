package org.progmob.langsupport

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class StatsActivity: Fragment() {

    var mycontext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mycontext = container?.context
        return inflater.inflate(R.layout.activity_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createPieChart()
    }

    private fun createPieChart(){

        val pie = getView()?.findViewById<PieChart>(R.id.piechart)

        pie?.addPieSlice(PieModel("Guessed", 10F, Color.parseColor("#ff0000")))
        pie?.addPieSlice(PieModel("Wrong", 20F, Color.parseColor("#0000ff")))
    }
}