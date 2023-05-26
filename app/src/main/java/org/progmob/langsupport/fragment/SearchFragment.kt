package org.progmob.langsupport.fragment

import android.content.Context
import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.progmob.langsupport.GuessPopUp
import org.progmob.langsupport.activity.DataActivity
import org.progmob.langsupport.adapter.historylist.HistoryListAdapter
import org.progmob.langsupport.adapter.searchlist.SearchListAdapter
import org.progmob.langsupport.databinding.FragmentSearchBinding
import org.progmob.langsupport.model.DataViewModel


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var historyListAdapter: HistoryListAdapter
    private lateinit var searchText: Editable
    private val viewModel: DataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchText = binding.searchEdit.text
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Search list adapter accepts a click event listener as parameter
        historyListAdapter = HistoryListAdapter()
        searchListAdapter = SearchListAdapter {
            GuessPopUp(it).show((activity as AppCompatActivity).supportFragmentManager, "showPop")
            binding.searchEdit.clearFocus()
        }

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
                    el -> el.wordIndex.equals(searchText.toString().trim(), ignoreCase = true)
            } == 0
        }

        viewModel.historyWords.observe(viewLifecycleOwner) {
            historyListAdapter.setWordsList(it)
        }

        binding.searchEdit.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.fetchWords(s?.trim())
            }

            override fun afterTextChanged(s: Editable?) {
                binding.historySection.visibility = if(s.isNullOrEmpty()) View.VISIBLE else View.GONE
                binding.listSection.visibility = if(s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

        })

        /* ----- Listeners ----- */

        binding.addButton.setOnClickListener {
            startActivity(Intent(requireContext(), DataActivity::class.java))
        }

        // Hide keyboard when the EditText loses focus
        binding.searchEdit.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }


        /* ----- Temporary ----- */
        viewModel.translatedWord.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.translateButton.setOnClickListener {
            binding.searchEdit.setText("")
            binding.searchEdit.clearFocus()
        }

        binding.translateButton.setOnLongClickListener {
            viewModel.translateWord(binding.searchEdit.text.toString(), "de")
            true
        }
    }
}