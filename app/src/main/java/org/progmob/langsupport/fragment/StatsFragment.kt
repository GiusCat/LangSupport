package org.progmob.langsupport.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import org.progmob.langsupport.R
import org.progmob.langsupport.databinding.FragmentStatsBinding
import org.progmob.langsupport.model.DataViewModel

class StatsFragment: Fragment() {

    var mycontext: Context? = null
    private lateinit var binding: FragmentStatsBinding
    private val viewModel: DataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mycontext = container?.context
        binding = FragmentStatsBinding.inflate(inflater, container, false)

        return binding.root
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         viewModel.getSearWords()

         viewModel.stats_data.observe(viewLifecycleOwner){
             binding.paroleCercateValue.text = viewModel.stats_data.value?.searched.toString()
             binding.paroleIndovinateValue.text = viewModel.stats_data.value?.guessed.toString()
             binding.paroleSbagliateValue.text = viewModel.stats_data.value?.wronged.toString()

             createPieChart(viewModel.stats_data.value?.guessed!!, viewModel.stats_data.value?.wronged!!)
         }

    }

    private fun createPieChart(indovinate: Int, sbagliate: Int) {

        binding.piechart.clearChart()

        binding.piechart.addPieSlice(PieModel("Guessed", indovinate.toFloat(), Color.parseColor("#FF00FF00")))
        binding.piechart.addPieSlice(PieModel("Wrong", sbagliate.toFloat(), Color.parseColor("#FFFF0000")))

        binding.piechart.startAnimation()


    }
}