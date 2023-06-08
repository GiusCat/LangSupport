package org.progmob.langsupport.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.progmob.langsupport.adapter.prefslist.PrefsListAdapter
import org.progmob.langsupport.databinding.FragmentPrefsBinding
import org.progmob.langsupport.model.DataViewModel

class PrefsFragment: Fragment() {
    private lateinit var binding: FragmentPrefsBinding
    private lateinit var favouriteListAdapter: PrefsListAdapter
    private val viewModel:DataViewModel by activityViewModels()

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

        favouriteListAdapter = PrefsListAdapter {
            viewModel.updateFavouriteWord(it)
        }

        binding.favouritesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouriteListAdapter
        }

        viewModel.activeFavWords.observe(viewLifecycleOwner) {
            favouriteListAdapter.setFavWordsList(it)
        }

        binding.filterEdit.addTextChangedListener(object : TextWatcher {

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               viewModel.getFavWordsLike(s?.trim())
           }

           override fun afterTextChanged(s: Editable?) {}

        })
    }
}