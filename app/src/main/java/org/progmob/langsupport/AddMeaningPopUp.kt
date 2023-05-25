package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class AddMeaningPopUp(tedText: String) : DialogFragment(){

    val tedText:String = tedText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.add_meaning_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textWord = view.findViewById<TextView>(R.id.wordToAddMean)
        textWord.setText(tedText)

        val addMeanButton = view.findViewById<Button>(R.id.addMeaningButton)
        val exit = view.findViewById<Button>(R.id.exitMeaningButton)

        addMeanButton.setOnClickListener {

        }

        exit.setOnClickListener {
            this.dismiss()
        }
    }
}