package org.progmob.langsupport

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import org.progmob.langsupport.adapter.searchlist.SearchListAdapter
import org.progmob.langsupport.databinding.FragmentSearchBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData


class SearchFragment : Fragment() {
    // TODO: TranslatorViewModel (?) and TranslatorRepository
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var searchText: Editable
    private val viewModel: DataViewModel by viewModels()

    private var translator:Translator? = null
    private var wordTr:String? = null
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

        binding.translateButton.setOnClickListener {
            verifyTranslateWord()
        }

        val model = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.GERMAN).setTargetLanguage(TranslateLanguage.ITALIAN).build()

        val itaGerTrans: Translator = Translation.getClient(model)

        val condition = DownloadConditions.Builder().requireWifi().build()
        itaGerTrans.downloadModelIfNeeded(condition).addOnSuccessListener { Log.i(MSG, "download completed") }

        this.translator = itaGerTrans

        val wordsList = initWordsList()

        if(wordsList.size > 0){

            val adapter = MainActivityAdapter(requireContext(), R.layout.activity_list_item, wordsList)

            val listView : ListView? = getView()?.findViewById(R.id.listViewLastWords)
            listView?.adapter = adapter
            adapter.notifyDataSetChanged()
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

        searchListAdapter = SearchListAdapter { verifyTranslateWord() }
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchListAdapter
        }

        viewModel.loadedWords.observe(viewLifecycleOwner) {
            searchListAdapter.setWordsList(it.toList())

            // I can't add a word which is already added
            binding.addButton.isEnabled = it.count {
                el -> el.wordIndex.equals(searchText.toString().trim(), ignoreCase = true)
            } == 0
        }
    }

    // inizializzazione della lista tedesco-italiano
    private fun initWordsList():MutableList<WordData>{
        return mutableListOf<WordData>().apply {
            add(WordData("apfel", listOf("mela")))
            add(WordData("hallo", listOf("ciao")))
            add(WordData("danke", listOf("grazie")))
        }
    }

    private fun verifyTranslateWord() {

        val wordToTraslate = view?.findViewById<EditText>(R.id.search_edit)
        this.wordTr = wordToTraslate?.text.toString()

        // aggiungere ricerca in DB e settare correttamente variabile sotto
        val already_searched: Boolean = true

        if(already_searched){

            val showPop = PopUp(wordTr!!, translator)
            showPop.show((activity as AppCompatActivity).supportFragmentManager, "showPop")

        }
        else {

            this.translator?.translate(wordToTraslate?.text.toString())?.addOnSuccessListener {

                    translatedText ->
                modifyListWords(translatedText)

            }?.addOnFailureListener { exception -> Log.i(MSG, "not found") }

        }

    }
    public fun modifyListWords(translatedWord: String){

        val newWords = mutableListOf<WordData>()
        val wordToTraslate = getView()?.findViewById<EditText>(R.id.search_edit)

        //prendiamo le prime due righe della vecchia lista
        // per metterle al secondo e terzo posto

        val oldList: ListView? = getView()?.findViewById(R.id.listViewLastWords)
        val oldFirst: View? = oldList?.getChildAt(0)
        val oldFirstWord:TextView? = oldFirst?.findViewById(R.id.foreign_word)
        val oldSecondWord:TextView? = oldFirst?.findViewById(R.id.translated_word)

        val oldSecond: View? = oldList?.getChildAt(1)
        val oldFirstWord2:TextView? = oldSecond?.findViewById(R.id.foreign_word)
        val oldSecondWord2:TextView? = oldSecond?.findViewById(R.id.translated_word)


        //aggiungiamo la nuova parola cercata al primo posto e le due vecchie di seguito
        if(wordToTraslate?.text.toString() == translatedWord)
            newWords.add(WordData(wordTr.toString(), listOf("not found")))
        else
            newWords.add(WordData(wordTr.toString(), listOf(translatedWord)))

        newWords.add(WordData(oldFirstWord?.text.toString(), listOf(oldSecondWord?.text.toString())))
        newWords.add(WordData(oldFirstWord2?.text.toString(), listOf(oldSecondWord2?.text.toString())))


        //chiamate di default per adapter

        val adapter = MainActivityAdapter(requireContext(), R.layout.activity_list_item, newWords)
        Log.i(MSG, translatedWord)
        val listView : ListView? = getView()?.findViewById(R.id.listViewLastWords)
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
        Log.i(MSG, translatedWord)

        //svuotiamo la casella di inserimento dopo l'inserimento in lista
        wordToTraslate?.setText("")
    }
    override fun onDestroy() {
        super.onDestroy()
        this.translator?.close()
    }


    companion object {
        val MSG: String? = this::class.simpleName
    }
}