package com.example.entreefederatie.data

import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

const val JS_PROPERTIES = "ReadReferentieProperties.js"

class LoginWebViewClient(val context: Context, val delegate: LoginDelegate): WebViewClient() {

    private val cookieManager = CookieStorage(context)

    init {
        cookieManager.loginDelegate = delegate
        cookieManager.loadCookies()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if (url != null){
            val cookies = CookieManager.getInstance().getCookie(url)
            cookieManager.saveCookies(cookies, url)
            delegate.propertiesPageLoaded()
        }
    }
}