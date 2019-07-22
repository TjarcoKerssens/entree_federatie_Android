package com.kennisnet.wikiwijs

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import com.kennisnet.entreefederatie.data.CookieStorage
import com.kennisnet.entreefederatie.data.LoginDelegate
import com.kennisnet.entreefederatie.data.LoginWebViewClient
import java.io.File

const val AUTH_ENDPOINT = "https://maken.wikiwijs.nl/"
const val SHARED_PACKAGE = "com.kennisnet.entreefederatie"

class MainActivity : AppCompatActivity(), LoginDelegate {
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLoginWebView()
    }

    /**
     * Setup the WebView to show the login screen.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupLoginWebView() {
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.webViewClient = LoginWebViewClient(sharedContext(), this)
    }

    private fun sharedContext() : Context{
        return createPackageContext(SHARED_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
    }

    override fun userIsAuthenticated() {
    }

    override fun cookiesSet() {
        webView.loadUrl(AUTH_ENDPOINT)
    }
}
