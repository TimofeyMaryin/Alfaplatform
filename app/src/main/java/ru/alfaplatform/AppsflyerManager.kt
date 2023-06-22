package ru.alfaplatform

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib

object AppsflyerManager {
    private const val APPSFLYER_ID = "FVS24b72a7nVw7ZimgMj5H"
    private lateinit var appContext: Context

    private var sub1: String = "None"
    private var sub2: String = "None"
    private var sub3: String = "None"
    private var sub4: String = "None"
    private var sub5: String = "None"

    fun init(context: Context){
        appContext = context.applicationContext

        val appsflyer = AppsFlyerLib.getInstance()
        appsflyer.setDebugLog(true)
        appsflyer.setMinTimeBetweenSessions(0)
        AppsFlyerLib.getInstance().setAppInviteOneLink("H5hv")

        appsflyer.init(APPSFLYER_ID, conversionDataListener, context)
        appsflyer.start(context)
    }

    fun initWithSubIds(context: Context, sub1: String?, sub2: String?, sub3: String?, sub4: String?, sub5: String?) {
        this.sub1 = sub1 ?: "None"
        this.sub2 = sub2 ?: "None"
        this.sub3 = sub3 ?: "None"
        this.sub4 = sub4 ?: "None"
        this.sub5 = sub5 ?: "None"

        val sharedPrefs = context.getSharedPreferences("AppsflyerData", Context.MODE_PRIVATE)
        with (sharedPrefs.edit()) {
            putString("sub1", this@AppsflyerManager.sub1)
            putString("sub2", this@AppsflyerManager.sub2)
            putString("sub3", this@AppsflyerManager.sub3)
            putString("sub4", this@AppsflyerManager.sub4)
            putString("sub5", this@AppsflyerManager.sub5)
            apply()
        }
    }

    private val conversionDataListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(conversionData: MutableMap<String, Any>?) {
            Log.e("onConversionDataSuccess", "conversionData.size = ${conversionData?.size}", )

            val sub1: String? = conversionData?.get("sub1") as? String
            val sub2: String? = conversionData?.get("sub2") as? String
            val sub3: String? = conversionData?.get("sub3") as? String
            val sub4: String? = conversionData?.get("sub4") as? String
            val sub5: String? = conversionData?.get("sub5") as? String

            val utmCampaign: String? = conversionData?.get("campaign") as? String
            val utmSource: String? = conversionData?.get("source") as? String
            val utmMedium: String? = conversionData?.get("medium") as? String
            val utmContent: String? = conversionData?.get("content") as? String
            val utmTerm: String? = conversionData?.get("term") as? String

            val eventValue: Map<String, Any> = mapOf(
                "af_sub1" to (sub1 ?: "None"),
                "af_sub2" to (sub2 ?: "None"),
                "af_sub3" to (sub3 ?: "None"),
                "af_sub4" to (sub4 ?: "None"),
                "af_sub5" to (sub5 ?: "None"),
                "utm_campaign" to (utmCampaign ?: "None"),
                "utm_source" to (utmSource ?: "None"),
                "utm_medium" to (utmMedium ?: "None"),
                "utm_content" to (utmContent ?: "None"),
                "utm_term" to (utmTerm ?: "None")
            )

            AppsFlyerLib.getInstance().logEvent(appContext, "af_purchase", eventValue)

            Log.e("onConversionDataSuccess", "-".repeat(20), )
            Log.e("onConversionDataSuccess", eventValue.values.joinToString(" --;-- "), )
            Log.e("onConversionDataSuccess", eventValue.entries.joinToString(" ; "), )
            Log.e("onConversionDataSuccess", conversionData?.entries?.joinToString(" ; ") ?: "Null", )
            Log.e("onConversionDataSuccess", conversionData?.values?.joinToString(" ; ") ?: "Null", )
            Log.e("onConversionDataSuccess", "-".repeat(20), )
        }
        override fun onConversionDataFail(error: String?) {
            Log.e("onConversionDataFail", "error onAttributionFailure :  $error")
        }

        override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
            data?.map {
                Log.d("onAppOpenAttribution", "onAppOpen_attribute: ${it.key} = ${it.value}")
            }
        }

        override fun onAttributionFailure(error: String?) {
            Log.e("onAttributionFailure", "error onAttributionFailure :  $error")
        }
    }
}