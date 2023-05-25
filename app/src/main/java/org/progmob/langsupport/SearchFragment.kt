package org.progmob.langsupport

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.progmob.langsupport.adapter.searchlist.SearchListAdapter
import org.progmob.langsupport.databinding.FragmentSearchBinding
import org.progmob.langsupport.model.DataViewModel


class SearchFragment : Fragment() {
    // TODO: TranslatorViewModel (?) and TranslatorRepository
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchListAdapter: SearchListAdapter
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

        // Adapter accepts an event listener as parameter
        searchListAdapter = SearchListAdapter {
            GuessPopUp(it).show((activity as AppCompatActivity).supportFragmentManager, "showPop")
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchListAdapter
        }

        /* ----- Observers ----- */

        viewModel.loadedWords.observe(viewLifecycleOwner) {
            searchListAdapter.setWordsList(it.toList())

            // I can't add a word which is already added
            binding.addButton.isEnabled = it.count {
                    el -> el.wordIndex.equals(searchText.toString().trim(), ignoreCase = true)
            } == 0
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


        /* ----- Temporary ----- */
        viewModel.translatedWord.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.translateButton.setOnClickListener {
            viewModel.translateWord(binding.searchEdit.text.toString(), "de")
        }
    }
}