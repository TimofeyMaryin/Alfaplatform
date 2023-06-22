package ru.alfaplatform

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import ru.alfaplatform.ui.theme.AlfaPlatformTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppsflyerManager.init(this)
        setContent { MainScreen(this) }
    }

    fun initSubIds(sub1: String?, sub2: String?, sub3: String?, sub4: String?, sub5: String?) {
        AppsflyerManager.initWithSubIds(this, sub1, sub2, sub3, sub4, sub5)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainActivity: MainActivity) {
    AlfaPlatformTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {},
                content = { CustomWebView(mainActivity); it }
            )
        }
    }
}


@Composable
fun CustomWebView(mainActivity: MainActivity) {

    val sharedPrefs = mainActivity.getSharedPreferences("AppsflyerData", Context.MODE_PRIVATE)

    val sub1 = sharedPrefs.getString("sub1", "None")!!
    val sub2 = sharedPrefs.getString("sub2", "None")!!
    val sub3 = sharedPrefs.getString("sub3", "None")!!
    val sub4 = sharedPrefs.getString("sub4", "None")!!
    val sub5 = sharedPrefs.getString("sub5", "None")!!

    val utm = "${sub1}_${sub2}_${sub3}_${sub4}_${sub5}"
    val neededUrl = "https://alf-iov.ru/?utm_campaign=${utm}"

    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                        Log.d("CustomWebView", "Loading URL: ${request.url}")
                        mainActivity.initSubIds(sub1, sub2, sub3, sub4, sub5)
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(neededUrl)
            }
        },
        update = {
            Log.d("CustomWebView", "Updating URL: $neededUrl")
            it.loadUrl(neededUrl)
        },
    )
}