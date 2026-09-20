package com.currencyconverter.app.data

import android.content.Context

class PrefsStore(context: Context) {
    private val prefs = context.getSharedPreferences("converter", Context.MODE_PRIVATE)

    fun loadAmount(): String = prefs.getString(KEY_AMOUNT, "100") ?: "100"

    fun loadBase(): String = prefs.getString(KEY_BASE, "ILS") ?: "ILS"

    fun loadLanguage(): String = prefs.getString(KEY_LANGUAGE, "he") ?: "he"

    fun loadTargets(): Set<String> {
        val raw = prefs.getString(KEY_TARGETS, "USD,EUR,GBP") ?: "USD,EUR,GBP"
        return raw.split(",")
            .map { it.trim().uppercase() }
            .filter { it.isNotEmpty() }
            .toSet()
            .ifEmpty { setOf("USD", "EUR", "GBP") }
    }

    fun loadRates(): Map<String, Double> {
        val raw = prefs.getString(KEY_RATES, "") ?: return emptyMap()
        if (raw.isBlank()) return emptyMap()
        return raw.split(";").mapNotNull { part ->
            val pieces = part.split("=")
            if (pieces.size != 2) return@mapNotNull null
            val value = pieces[1].toDoubleOrNull() ?: return@mapNotNull null
            pieces[0] to value
        }.toMap()
    }

    fun loadRatesAt(): Long? {
        val value = prefs.getLong(KEY_RATES_AT, -1L)
        return if (value > 0) value else null
    }

    fun save(amount: String, base: String, targets: Set<String>, language: String) {
        prefs.edit()
            .putString(KEY_AMOUNT, amount)
            .putString(KEY_BASE, base)
            .putString(KEY_TARGETS, targets.joinToString(","))
            .putString(KEY_LANGUAGE, language)
            .apply()
    }

    fun saveRates(rates: Map<String, Double>, updatedAt: Long) {
        val raw = rates.entries.joinToString(";") { "${it.key}=${it.value}" }
        prefs.edit()
            .putString(KEY_RATES, raw)
            .putLong(KEY_RATES_AT, updatedAt)
            .apply()
    }

    private companion object {
        const val KEY_AMOUNT = "amount"
        const val KEY_BASE = "base"
        const val KEY_TARGETS = "targets"
        const val KEY_LANGUAGE = "language"
        const val KEY_RATES = "rates"
        const val KEY_RATES_AT = "rates_at"
    }
}
