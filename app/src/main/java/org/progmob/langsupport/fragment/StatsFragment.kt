package org.progmob.langsupport.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.eazegraph.lib.models.PieModel
import org.progmob.langsupport.databinding.FragmentStatsBinding
import org.progmob.langsupport.model.DataViewModel

class StatsFragment: Fragment() {
    private lateinit var binding: FragmentStatsBinding
    private val viewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         viewModel.getStatsData()

         viewModel.statsData.observe(viewLifecycleOwner){
             val wronged = it.searched - it.guessed
             binding.paroleCercateValue.text = it.searched.toString()
             binding.paroleIndovinateValue.text = it.guessed.toString()
             binding.paroleSbagliateValue.text = wronged.toString()
             createPieChart(it.guessed, wronged)
         }
    }

    private fun createPieChart(guessed: Int, wronged: Int) {
        binding.piechart.clearChart()
        binding.piechart.addPieSlice(PieModel("Guessed", guessed.toFloat(), Color.GREEN))
        binding.piechart.addPieSlice(PieModel("Wrong", wronged.toFloat(), Color.RED))
        binding.piechart.startAnimation()
    }
}