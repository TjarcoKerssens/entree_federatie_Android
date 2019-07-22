package com.kennisnet.entreefederatie.data

import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import com.kennisnet.entreefederatie.models.Cookie
import com.kennisnet.wikiwijs.AUTH_ENDPOINT
import java.io.File
import java.nio.charset.Charset

const val COOKIES_FILE = "EntreeCookies"
const val SESSION_COOKIE_NAME = "SimpleSAMLAuthToken"

interface LoginDelegate{
    fun userIsAuthenticated()
    fun cookiesSet()
}

/**
 * A class that manages the cookies on the storage and loads them into the CookieManager
 */
class CookieStorage(private val context: Context){
    var loginDelegate: LoginDelegate? = null
    val cookieManager: CookieManager = CookieManager.getInstance()

    /**
     * This method will save the cookies to a file, alongside with the URL where the cookies are meant for.
     *
     * The cookies are stored in the format: c1=v1;c2=v2...&url
     *
     * If the cookies contain a valid session, the loginDelegate set on this class will be called.
     *
     * @param cookies A String retrieved from a CookieManager, i.e. cookieName=cookievalue;cookiename2=cookievalue2
     * @param url the base URl where the cookies are set for
     */
    fun saveCookies(cookies: String, url:String){
        val cookiesString = "$cookies&$url"
        if (validateCookies(parseCookies(cookiesString))){
            val file = File(context.filesDir, COOKIES_FILE)
            file.writeText(cookiesString, Charset.defaultCharset())
        }
    }

    /**
     * Load the cookies from storage into the CookieManager, making the cookies available in the WebView.
     *
     * Use this function to restore a locally saved session, or to load a shared session to enable SSO.
     */
    fun loadCookies(){
        val file  = File(context.filesDir, COOKIES_FILE)
        if (file.exists()){
            val cookiesString = file.readText()
            setCookies(parseCookies(cookiesString))
        }

    }

    /**
     * Remove the cookies file and all the cookies from the CookieManager.
     */
    fun removeSession(){
        CookieManager.getInstance().removeAllCookies(null)
        val file = File(context.filesDir, COOKIES_FILE)
        file.delete()
    }

    /**
     * Parses a string into a list of `Cookie` objects.
     *
     * @param cookiesString A string in the format c1=v1;c2=v2;...&url
     * @return A list of cookies
     */
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
        val cookieCount = cookies.size
        var setCounter = 0
        for (cookie in cookies){
            cookieManager.setCookie(AUTH_ENDPOINT, "${cookie.name}=${cookie.value}") {
                setCounter++
                if(setCounter == cookieCount){
                    loginDelegate?.cookiesSet()
                }
                Log.d("Cookie","Cookie is set: $it")
            }
        }
        validateCookies(cookies)
    }

    private fun validateCookies(cookies: List<Cookie>): Boolean{
        if ( cookies.find{ cookie -> cookie.name == SESSION_COOKIE_NAME} != null){
            loginDelegate?.userIsAuthenticated()
            return true
        }
        return false
    }
}