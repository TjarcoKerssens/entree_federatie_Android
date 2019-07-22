package com.example.entreefederatie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.beust.klaxon.Klaxon
import com.example.entreefederatie.R
import com.example.entreefederatie.data.CookieStorage
import com.example.entreefederatie.models.ReferentieProperties

class MainActivity : AppCompatActivity() {
    var properties: ReferentieProperties? = null
    lateinit var nameView: TextView
    lateinit var uidView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        parseProperties()
        setProperties()
    }

    private fun initViews(){
        nameView = findViewById(R.id.nameTextView)
        uidView = findViewById(R.id.uidTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout(){
        CookieStorage(this).removeSession()
        finish()
    }

    private fun parseProperties(){
        val propertiesString = intent.getStringExtra("properties")
        properties = Klaxon().parse<ReferentieProperties>(propertiesString)
    }

    private fun setProperties(){
        val properties = properties
        if (properties != null){
            nameView.text = "Name: ${properties.givenName.trim()} ${properties.surname.trim()}"
            uidView.text = "UID: ${properties.uid.trim()}"
        }
    }
}
