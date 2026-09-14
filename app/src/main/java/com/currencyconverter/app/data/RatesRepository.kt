package com.currencyconverter.app.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.Instant
import java.util.concurrent.TimeUnit

data class RatesSnapshot(
    val base: String,
    val rates: Map<String, Double>,
    val sources: Map<String, String>,
    val marketSession: String?,
    val fetchedAtMillis: Long
)

class RatesRepository(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()
) {
    suspend fun fetchRates(base: String): RatesSnapshot = withContext(Dispatchers.IO) {
        val url = "https://api.exchangerate.dev/v1/latest/$base".toHttpUrl()
        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .header("User-Agent", "CurrencyConverter/1.0")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException("network:${response.code}")
            }
            val body = response.body?.string().orEmpty()
            parse(base, body)
        }
    }

    private fun parse(base: String, body: String): RatesSnapshot {
        val root = JSONObject(body)
        if (root.optString("result") != "success") {
            throw IllegalStateException("invalid")
        }
        val table = root.optJSONObject("rates") ?: throw IllegalStateException("invalid")
        val rates = linkedMapOf<String, Double>()
        rates[base.uppercase()] = 1.0
        val keys = table.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = table.optDouble(key, Double.NaN)
            if (!value.isNaN() && value > 0.0) {
                rates[key.uppercase()] = value
            }
        }
        val sources = linkedMapOf<String, String>()
        val sourcesJson = root.optJSONObject("sources")
        if (sourcesJson != null) {
            val sourceKeys = sourcesJson.keys()
            while (sourceKeys.hasNext()) {
                val key = sourceKeys.next()
                sources[key.uppercase()] = sourcesJson.optString(key)
            }
        }
        return RatesSnapshot(
            base = root.optString("base", base).uppercase(),
            rates = rates,
            sources = sources,
            marketSession = root.optString("market_session").ifBlank { null },
            fetchedAtMillis = parseTimestamp(root.optString("timestamp")) ?: System.currentTimeMillis()
        )
    }

    private fun parseTimestamp(value: String): Long? {
        if (value.isBlank()) return null
        return try {
            Instant.parse(value).toEpochMilli()
        } catch (_: Exception) {
            null
        }
    }
}
