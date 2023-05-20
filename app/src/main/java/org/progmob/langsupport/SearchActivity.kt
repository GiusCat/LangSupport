package org.progmob.langsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.progmob.langsupport.adapter.SearchListAdapter
import org.progmob.langsupport.databinding.ActivitySearchBinding
import org.progmob.langsupport.model.DataViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: DataViewModel by viewModels()
    private val searchListAdapter = SearchListAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.addTextChangedListener(object : TextWatcher {
            // private var lastUpdate: Long = System.currentTimeMillis()
            // private val refreshTime = 0 // milliseconds to wait for refresh

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.fetchWords(s)
            }

            override fun afterTextChanged(s: Editable?) {
                binding.historyLayout.visibility = if(s.isNullOrEmpty()) View.VISIBLE else View.GONE
                binding.listLayout.visibility = if(s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = searchListAdapter
        }

        viewModel.loadedWords.observe(this) {
            searchListAdapter.setWordsList(it.toList())
            // searchListAdapter.notifyDataSetChanged()
        }
    }
}