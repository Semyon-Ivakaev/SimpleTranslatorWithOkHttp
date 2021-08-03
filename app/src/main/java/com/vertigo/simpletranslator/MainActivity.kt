package com.vertigo.simpletranslator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*


class MainActivity : AppCompatActivity() {
    private val subscriptionKey = "API_KEY"

    // Add your location, also known as region. The default is global.
    // This is required if using a Cognitive Services resource.
    private val location = "LOCATION"

    private var url = HttpUrl.Builder()
        .scheme("https")
        .host("api.cognitive.microsofttranslator.com")
        .addPathSegment("/translate")
        .addQueryParameter("api-version", "3.0")
        .addQueryParameter("from", "en")
        .addQueryParameter("to", "ru")
        .build()

    // Instantiates the OkHttpClient.
    private var client = OkHttpClient()

    // This function performs a POST request.
    private fun post(text: String): String {
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(
            mediaType,
            "[{\"Text\": \"$text\"}]"
        )
        val request: Request = Request.Builder().url(url).post(body)
            .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
            .addHeader("Ocp-Apim-Subscription-Region", location)
            .addHeader("Content-type", "application/json")
            .build()
        val response = client.newCall(request).execute()
        return response.body()!!.string()
    }

    // This function prettifies the json response.
    private fun prettify(json_text: String?): String {
        val parser = JsonParser()
        val json = parser.parse(json_text)
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(json)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val originalText = findViewById<EditText>(R.id.editText)
        val btn = findViewById<Button>(R.id.btn)
        val translateText = findViewById<TextView>(R.id.textView)

        var text = ""

        btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                text = prettify(post(originalText.text.toString()))
                Log.v("AppVerbose", text)
            }
            translateText.text = text
        }

    }
}