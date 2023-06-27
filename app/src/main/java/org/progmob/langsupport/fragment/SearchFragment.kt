package org.progmob.langsupport.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.progmob.langsupport.AddWordPopUp
import org.progmob.langsupport.GuessPopUp
import org.progmob.langsupport.adapter.historylist.HistoryListAdapter
import org.progmob.langsupport.adapter.searchlist.SearchListAdapter
import org.progmob.langsupport.databinding.FragmentSearchBinding
import org.progmob.langsupport.model.DataViewModel


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var historyListAdapter: HistoryListAdapter
    private val viewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Search list adapter accepts a click event listener as parameter
        historyListAdapter = HistoryListAdapter()
        searchListAdapter = SearchListAdapter( {
            // Item click listener
            GuessPopUp(it).show((activity as AppCompatActivity).supportFragmentManager, "showPop")
            binding.searchEdit.clearFocus()
        }, {

            val isFav = !it.favourite
            viewModel.updateFavouriteWord(it)
            Toast.makeText(context,
                "${it.word} ${if(isFav) "added to" else "removed from"} favourites!",
                Toast.LENGTH_SHORT).show()
        }, {

            viewModel.deleteWord(it)
            Toast.makeText(context, "${it.word} deleted", Toast.LENGTH_SHORT).show()
        })

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchListAdapter
        }

        binding.historyRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyListAdapter
        }

        /* ----- Observers ----- */

        viewModel.activeWords.observe(viewLifecycleOwner) {
            searchListAdapter.setWordsList(it.toList())

            // I can't add a word which is already added
            binding.addButton.isEnabled = it.count {
                    el -> el.word.equals(binding.searchEdit.text.toString().trim(), ignoreCase = true)
            } == 0
        }

        viewModel.historyWords.observe(viewLifecycleOwner) {
            historyListAdapter.setWordsList(it)
        }

        binding.searchEdit.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.getWordsLike(s?.trim())
            }

            override fun afterTextChanged(s: Editable?) {
                binding.historySection.visibility = if(s.isNullOrEmpty()) View.VISIBLE else View.GONE
                binding.listSection.visibility = if(s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

        })

        /* ----- Listeners ----- */

        binding.addButton.setOnClickListener {
            val showPop = AddWordPopUp(binding.searchEdit.text.toString().trim())
            showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            binding.searchEdit.setText("")
        }

        binding.dismissButton.setOnClickListener {
            binding.searchEdit.setText("")
            binding.searchEdit.clearFocus()
        }

        // Hide keyboard when the EditText loses focus
        binding.searchEdit.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }
}