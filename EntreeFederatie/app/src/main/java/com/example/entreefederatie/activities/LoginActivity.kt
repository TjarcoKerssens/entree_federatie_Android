package com.example.entreefederatie.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.example.entreefederatie.R
import com.example.entreefederatie.data.JS_PROPERTIES
import com.example.entreefederatie.data.LoginDelegate
import com.example.entreefederatie.data.LoginWebViewClient
import java.io.BufferedReader


const val AUTH_ENDPOINT = "https://referentie.entree.kennisnet.nl/saml/module.php/core/authenticate.php?as=RefSPSAML"

class LoginActivity : AppCompatActivity(), LoginDelegate {
    private lateinit var loginWebView: WebView
    private var authenticated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onResume() {
        super.onResume()
        authenticated = false
        setupLoginWebView()
    }

    /**
     * Setup the WebView to show the login screen.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupLoginWebView() {
        loginWebView = findViewById(R.id.loginWebview)
        loginWebView.webViewClient = LoginWebViewClient(applicationContext, this)
        loginWebView.settings.javaScriptEnabled = true
        loginWebView.addJavascriptInterface(this, "android")
        loginWebView.loadUrl(AUTH_ENDPOINT)
    }

    override fun userIsAuthenticated() {
        if (!authenticated) {
            authenticated = true
        }
    }

    override fun propertiesPageLoaded() {
        if (authenticated){
            parseProperties()
        }
    }

    private fun parseProperties(){
        val javascript = "javascript:${readPropertiesJavaScript().trim()}"
        loginWebView.loadUrl(javascript)
    }

    private fun readPropertiesJavaScript(): String{
        val inputStream = assets.open(JS_PROPERTIES)
        return inputStream.bufferedReader().use(BufferedReader::readText)
    }

    private fun goToMainScreen(properties: String){
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("properties", properties)
        startActivity(mainIntent)
    }

    @JavascriptInterface
    fun propertiesFound(value: String){
        goToMainScreen(value)
    }
}
