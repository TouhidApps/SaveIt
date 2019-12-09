package com.touhidapps.saveitExample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.touhidapps.saveit.SaveIt
import com.touhidapps.saveit.SaveItS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val MY_NAME = "my_name"

    val MY_NAME_SECURED = "my_name_secured"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveItCode()

        saveItSCode()

    } // onCreate

    /**
     * General data
     */
    private fun saveItCode() {


        buttonSave.setOnClickListener {

            val name: String = editTextName.text.toString()
            SaveIt.putString(MY_NAME, name)

        }

        buttonShow.setOnClickListener {

            val name: String = SaveIt.getString(MY_NAME, "")
            textViewResult.text = name

        }

    } // saveItCode

    /**
     * Secure data
     */
    private fun saveItSCode() {

        buttonSaveS.setOnClickListener {

            val name: String = editTextNameS.text.toString()
            SaveItS.putString(MY_NAME_SECURED, name)

        }

        buttonShowS.setOnClickListener {

            val name: String = SaveItS.getString(MY_NAME_SECURED, "")
            textViewResultS.text = name

        }

    } // saveItSCode


}
