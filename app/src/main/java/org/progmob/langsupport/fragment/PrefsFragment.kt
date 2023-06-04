package org.progmob.langsupport.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import org.progmob.langsupport.adapter.prefslist.PrefsListAdapter
import org.progmob.langsupport.databinding.FragmentPrefsBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

class PrefsFragment: Fragment() {

    private lateinit var binding: FragmentPrefsBinding
    private lateinit var prefsListAdapter: PrefsListAdapter
    private val viewModel:DataViewModel by activityViewModels()
    var prefsList: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrefsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsListAdapter = PrefsListAdapter(viewModel)


        binding.listViewPrefs.apply {

            layoutManager = LinearLayoutManager(requireContext())
            adapter = prefsListAdapter
        }

        /*viewModel.prefsData.observe(viewLifecycleOwner){
            prefsListAdapter.setWordsList(it.toList())
        }*/

        binding.searchPrefsButton.setOnClickListener {
            viewModel.prefsData.observe(viewLifecycleOwner){
                prefsListAdapter.setWordsList(it.toList())
            }
            //SearchWordInDB()
        }

       viewModel.activeWordsPrefs.observe(viewLifecycleOwner){

           binding.InsertWordPrefs.addTextChangedListener(object : TextWatcher {

               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                   viewModel.fetchWordsPrefs(s?.trim())
                   prefsListAdapter.setWordsList(it.toList())
               }

               override fun afterTextChanged(s: Editable?) {}
           })

       }

        Log.i("lista preferiti", prefsList.value.toString())

    }

    private fun SearchWordInDB() {
        TODO("Not yet implemented")

    }
}