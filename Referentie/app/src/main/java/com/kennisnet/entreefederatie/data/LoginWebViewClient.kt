package com.kennisnet.entreefederatie.data

import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient


/**
 * A class to respond to events on the LoginWebView. It makes sure that the cookies are loaded if they exist.
 */
class LoginWebViewClient(context: Context, private val delegate: LoginDelegate): WebViewClient() {

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