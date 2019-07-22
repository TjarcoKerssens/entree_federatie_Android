package com.example.entreefederatie.data

import android.content.Context
import android.webkit.CookieManager
import com.example.entreefederatie.models.Cookie
import java.io.File
import java.nio.charset.Charset

const val COOKIES_FILE = "EntreeCookies"
const val SESSION_COOKIE_NAME = "SimpleSAMLAuthToken"

interface LoginDelegate{
    fun userIsAuthenticated()
    fun propertiesPageLoaded()
}

class CookieStorage(private val context: Context){
    var loginDelegate: LoginDelegate? = null

    fun saveCookies(cookies: String, url:String){
        val file = File(context.filesDir, COOKIES_FILE)
        val cookiesString = "$cookies&$url"
        file.writeText(cookiesString, Charset.defaultCharset())
        validateCookies(parseCookies(cookiesString))
    }

    fun loadCookies(){
        val file  = File(context.filesDir, COOKIES_FILE)
        if (file.exists()){
            val cookiesString = file.readText()
            setCookies(parseCookies(cookiesString))
        }

    }

    fun removeSession(){
        CookieManager.getInstance().removeAllCookies(null)
        val file = File(context.filesDir, COOKIES_FILE)
        file.delete()
    }

    private fun parseCookies(cookiesString: String): List<Cookie>{
        val data = cookiesString.split("&")
        val url = data[1]
        val cookiePairs = data[0].split(";")
        val cookies = mutableListOf<Cookie>()
        for (cookieString in cookiePairs){
            val cookiePair = cookieString.split("=")
            if (cookiePair.size >= 2){
                cookies.add(Cookie(cookiePair[0].trim(), cookiePair[1].trim(), url))
            }
        }
        return cookies
    }

    private fun setCookies(cookies: List<Cookie>){
        val cookieManager = CookieManager.getInstance()
        for (cookie in cookies){
            cookieManager.setCookie(cookie.url, "${cookie.name}=${cookie.value}")
        }
        validateCookies(cookies)
    }

    private fun validateCookies(cookies: List<Cookie>){
        if ( cookies.find{ cookie -> cookie.name == SESSION_COOKIE_NAME} != null){
            loginDelegate?.userIsAuthenticated()
        }
    }
}