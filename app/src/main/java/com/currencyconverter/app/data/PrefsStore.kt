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

    fun save(amount: String, base: String, targets: Set<String>, language: String) {
        prefs.edit()
            .putString(KEY_AMOUNT, amount)
            .putString(KEY_BASE, base)
            .putString(KEY_TARGETS, targets.joinToString(","))
            .putString(KEY_LANGUAGE, language)
            .apply()
    }

    private companion object {
        const val KEY_AMOUNT = "amount"
        const val KEY_BASE = "base"
        const val KEY_TARGETS = "targets"
        const val KEY_LANGUAGE = "language"
    }
}
