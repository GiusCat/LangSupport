package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment

class Right_PopUp(translation: String, tedText: String, right: Boolean) : DialogFragment() {

    val trans = translation
    val rigth = right
    val tedText = tedText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.guessed_pop_up, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textRight = view.findViewById<TextView>(R.id.textGuessedFragment)
        val textIta = view.findViewById<TextView>(R.id.itaWordGuessed)
        val textTed = view.findViewById<TextView>(R.id.tedWordGuessed)
        val c : ConstraintLayout  = view.findViewById<ConstraintLayout>(R.id.section1frag)

        if(rigth) {
            textRight.setText("Eccellente! Hai indovinato")
            c.setBackgroundColor(getResources().getColor(R.color.lightGreen))
        }
        else{

            textRight.setText("Peccato! Hai Sbagliato")
            c.setBackgroundColor(getResources().getColor(R.color.red))

        }
        textIta.setText(trans)
        textTed.setText(tedText)
        val exitButton = view.findViewById<Button>(R.id.exit)
        val addMeaning = view.findViewById<Button>(R.id.addMean)

        exitButton.setOnClickListener {
            this.dismiss()
        }

        addMeaning.setOnClickListener {

            val showPop = AddMeaningPopUp(tedText)
            showPop.show((activity as AppCompatActivity).supportFragmentManager, "addMean")
        }

    }
}