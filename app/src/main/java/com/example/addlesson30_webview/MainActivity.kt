package com.example.addlesson30_webview

import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

private const val BASE_PREFIX = "https://"

class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_url.setOnEditorActionListener(this)
        setUpWEbView()

        ib_left.setOnClickListener {
            onBackPressed() 
        }
        ib_forward.setOnClickListener {
            onGoForward()
        }
    }

    private fun onGoForward() {
        if (web_view.canGoForward()) web_view.goForward()
    }


    private fun setUpWEbView() {
        web_view.webViewClient = MyWebViewClient()
        web_view.webChromeClient = MyWebChromeClient()
    }

    inner class MyWebChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressBar.max = 100
            super.onProgressChanged(view, newProgress)
            progressBar.progress = newProgress
        }

    }

    inner class MyWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progressBar.visibility = View.VISIBLE
            progressBar.progress = 0
            super.onPageStarted(view, url, favicon)
            et_url.setText(url)

        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            updateUrl(request?.url.toString())
            return super.shouldOverrideUrlLoading(view, request)
        }


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }

    }

    private fun updateUrl(url: String) {
        et_url.setText(url)
    }

    override fun onEditorAction(
        v: TextView?,
        actionId: Int,
        event: KeyEvent?
    ): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            loadNewUrl()
            return true
        }
        return false
    }

    private fun loadNewUrl() {
        val urlBase = et_url.text.toString()
        web_view.loadUrl("$BASE_PREFIX$urlBase")
        et_url.hideKeyboard()

    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) web_view.goBack()
        else super.onBackPressed()

    }


}


